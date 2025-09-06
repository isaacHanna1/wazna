package com.watad.controller;

import com.watad.dto.MarketItemDto;
import com.watad.entity.MarketItem;
import com.watad.services.MarketCategoryService;
import com.watad.services.MarketItemService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/market")
public class MarketController {

    private final MarketCategoryService marketCategoryService;
    private final MarketItemService marketItemService;

    public MarketController(MarketCategoryService marketCategoryService, MarketItemService marketItemService) {
        this.marketCategoryService = marketCategoryService;
        this.marketItemService = marketItemService;
    }

    @GetMapping("/add")
    public String createItemView(Model model){

        model.addAttribute("category" , marketCategoryService.allActiveCategory());
        model.addAttribute("item",new MarketItem());
        return "martketItem";
    }

    @PostMapping("/add")
    public String addItem( @ModelAttribute("item") MarketItem item,  Model model,
                            @RequestParam("image") MultipartFile image) {
        marketItemService.saveItem(item,image);
        return "redirect:/market/item/all?pageNum=1&pageSize=10";
    }

    @GetMapping("/item/all")
    public String getAllItem(Model model ,@RequestParam int pageNum , @RequestParam int pageSize){
        List<MarketItemDto> marketItems =  marketItemService.getMarketItem(pageNum,pageSize);
        model.addAttribute("items" ,marketItems);
        int numberOfItems = marketItems.size();
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
}
