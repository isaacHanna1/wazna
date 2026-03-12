package com.watad.controller;

import com.watad.dto.MarketItemDto;
import com.watad.entity.MarketItem;
import com.watad.services.MarketCategoryService;
import com.watad.services.MarketItemService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.lang.reflect.MalformedParametersException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/market")
public class MarketController {

    private final MarketCategoryService marketCategoryService;
    private final MarketItemService marketItemService;

    @Value("${file.uploadMarket-dir}")
    private String marketImagesPath;

    public MarketController(MarketCategoryService marketCategoryService, MarketItemService marketItemService) {
        this.marketCategoryService = marketCategoryService;
        this.marketItemService = marketItemService;
    }

    @GetMapping("/add")
    public String createItemView(Model model) {
        model.addAttribute("category", marketCategoryService.allActiveCategory());
        model.addAttribute("item",     new MarketItem());
        model.addAttribute("isEdit",   false);
        model.addAttribute("pageNum",  1);
        model.addAttribute("pageSize", 12);
        return "martketItem";
    }

    @PostMapping("/add")
    public String addItem(
            @ModelAttribute("item") MarketItem item,
            @RequestParam(value = "image", required = false) MultipartFile image,
            @RequestParam(defaultValue = "1")  int pageNum,
            @RequestParam(defaultValue = "12") int pageSize,
            RedirectAttributes redirectAttributes) throws IOException {

        marketItemService.saveItem(item, image);
        redirectAttributes.addFlashAttribute("msg",  "Item added successfully");
        redirectAttributes.addFlashAttribute("type", "success");

        return String.format("redirect:/market/item/all?pageNum=%d&pageSize=%d", pageNum, pageSize);
    }
    @GetMapping("/item/all")
    public String getAllItem(Model model ,@RequestParam int pageNum , @RequestParam int pageSize){
        List<MarketItemDto> marketItems =  marketItemService.getMarketItem(pageNum,pageSize);
        model.addAttribute("items" ,marketItems);
        int numberOfItems = marketItemService.getMarketItemCount();
        int numOfPages     = numberOfItems/pageSize + 1 ;
        model.addAttribute("numOfPages",numOfPages);
        model.addAttribute("currentPage",pageNum);
        model.addAttribute("pageSize",pageSize);
        model.addAttribute("pageNum",pageNum);
        return "allItemView";
    }

    @GetMapping("/item/all/find")
    @ResponseBody
    public List<MarketItemDto> getAllItem(@RequestParam String keyword){
        List<MarketItemDto> marketItems = marketItemService.searchByItemNameOrDesc(keyword);
        return marketItems;
    }


    @GetMapping("/item/all/findItem")
    public String getElementById(@RequestParam (required = false) Integer itemId , @RequestParam(required = false) String itemName,Model model){
        List<MarketItemDto> list = null ;
        if(itemId  != null){
            MarketItemDto marketItemDto =   marketItemService.getItemById(itemId);
            list                        = new ArrayList<>() ;
            list.add(marketItemDto);
        }else if(itemName !=null){
            list = marketItemService.searchByItemNameOrDesc(itemName); // return the fifth matched records only
        }
        model.addAttribute("items",list);
        model.addAttribute("numOfPages",1);
        model.addAttribute("currentPage",1);
        model.addAttribute("pageSize",1);
        model.addAttribute("pageNum",1);
        return "allItemView";
    }

    @PutMapping("/item/update")
    public ResponseEntity<String> updateStatusBulk(@RequestBody List<MarketItemDto> updatedList){
        marketItemService.updateStatus(updatedList);
        return ResponseEntity.ok("Status updated successfully");
    }

    @GetMapping("/item/{itemId}")
    public String getItemView(
            @PathVariable int itemId,
            @RequestParam(defaultValue = "1")  int pageNum,
            @RequestParam(defaultValue = "12") int pageSize,
            Model model) {

        MarketItem marketItem = marketItemService.getEntityItemById(itemId);
        model.addAttribute("item",     marketItem);
        model.addAttribute("category", marketCategoryService.allActiveCategory());
        model.addAttribute("pageNum",  pageNum);
        model.addAttribute("pageSize", pageSize);
        return "martketItem";
    }

    @GetMapping("/item/image/{fileName:.+}")
    @ResponseBody
    public ResponseEntity<Resource> getImage(@PathVariable String fileName ) throws IOException {
        Path file            = Paths.get(marketImagesPath).resolve(fileName);
        Resource resource    = new UrlResource(file.toUri());
        if(resource.exists() || resource.isReadable()){
            String contentType = Files.probeContentType(file);
            if (contentType == null) {
                contentType = "application/octet-stream";
            }
            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .body(resource);
        }else {
            return ResponseEntity.notFound().build();
        }
    }
}
