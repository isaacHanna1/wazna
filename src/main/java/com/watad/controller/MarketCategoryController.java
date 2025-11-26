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

import java.util.List;

@Controller
@RequestMapping("/")
public class MarketCategoryController {

    private final MarketCategoryService marketCategoryService;
    private final MarketItemService marketItemService;


    private static final int PAGE_SIZE           = 12;

    public MarketCategoryController(MarketCategoryService marketCategoryService , MarketItemService marketItemService) {
        this.marketCategoryService = marketCategoryService;
        this.marketItemService     = marketItemService;
    }

     @GetMapping("market")
    public String getMarketHome(Model model){
         List<MarketCategory> allActiveCategory  = marketCategoryService.allActiveCategory();
        model.addAttribute("category" , allActiveCategory);
        int defaultCatNum = allActiveCategory.get(0).getId();
        // by default send the category one
        model.addAttribute("itemMarket",marketItemService.findByCategoryWithPagination(defaultCatNum,0,PAGE_SIZE));
        model.addAttribute("catCount",allActiveCategory.get(0));
        model.addAttribute("totalPages", (int) Math.ceil((double) marketItemService.countByCategory(defaultCatNum) / PAGE_SIZE));
        model.addAttribute("categoryId",defaultCatNum);
        return "marketHome";
    }
    @GetMapping("/market/category/{id}")
    public String getMarketCategory(Model model, @PathVariable int id, @RequestParam(defaultValue = "0") int page){
        model.addAttribute("category" , marketCategoryService.allActiveCategory());
        model.addAttribute("itemMarket",marketItemService.findByCategoryWithPagination(id,page,PAGE_SIZE));
        model.addAttribute("totalPages", (int) Math.ceil((double) marketItemService.countByCategory(id) / PAGE_SIZE));
        model.addAttribute("categoryId",id);
        return "marketHome";
    }
}
