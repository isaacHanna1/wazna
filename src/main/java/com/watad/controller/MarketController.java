package com.watad.controller;

import com.watad.entity.MarketItem;
import com.watad.entity.Profile;
import com.watad.services.MarketCategoryService;
import com.watad.services.MarketItemService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
        return "martketItem";
    }
}
