package com.watad.controller;

import com.watad.entity.MarketCategory;
import com.watad.services.MarketCategoryService;
import com.watad.services.MarketItemService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/")
public class MarketCategoryController {

    private final MarketCategoryService marketCategoryService;
    private final MarketItemService marketItemService;
    public MarketCategoryController(MarketCategoryService marketCategoryService , MarketItemService marketItemService) {
        this.marketCategoryService = marketCategoryService;
        this.marketItemService     = marketItemService;
    }

    @GetMapping("market")
    public String getMarketHome(Model model){
        model.addAttribute("category" , marketCategoryService.allActiveCategory());
        model.addAttribute("itemMarket",marketItemService.findByCategoryWithPagination(1,0,10));
        model.addAttribute("catCount",marketItemService.countByCategory(1));
        model.addAttribute("totalPages", (int) Math.ceil((double) marketItemService.countByCategory(1) / 10));
        model.addAttribute("categoryId","1");
        return "marketHome";
    }

    @GetMapping("/market/category/{id}")
    public String getMarketCategory(Model model, @PathVariable int id, @RequestParam int page){
        model.addAttribute("category" , marketCategoryService.allActiveCategory());
        model.addAttribute("itemMarket",marketItemService.findByCategoryWithPagination(id,page,10));
        model.addAttribute("totalPages", (int) Math.ceil((double) marketItemService.countByCategory(id) / 10));
        model.addAttribute("categoryId",id);

        return "marketHome";
    }
}
