package com.watad.controller;

import com.watad.dto.PointTransactionSummaryDto;
import com.watad.entity.MarketCategory;
import com.watad.services.MarketCategoryService;
import com.watad.services.MarketItemService;
import com.watad.services.UserPointTransactionService;
import com.watad.services.UserServices;
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
    private final UserServices userServices;
    private final UserPointTransactionService userPointTransactionService;

    private static final int PAGE_SIZE           = 12;

    public MarketCategoryController(MarketCategoryService marketCategoryService, MarketItemService marketItemService, UserServices userServices, UserPointTransactionService userPointTransactionService) {
        this.marketCategoryService = marketCategoryService;
        this.marketItemService = marketItemService;
        this.userServices = userServices;
        this.userPointTransactionService = userPointTransactionService;
    }

    @GetMapping("market")
    public String getMarketHome(Model model){
         List<MarketCategory> allActiveCategory  = marketCategoryService.allActiveCategory();
        model.addAttribute("category" , allActiveCategory);
        int defaultCatNum = allActiveCategory.get(0).getId();
        int profileId     = userServices.logedInUser().getProfile().getId();
        int sprintId      = userServices.getActiveSprint().getId();
        double points     = userPointTransactionService.getTotalPointsByProfileIdAndSprintId(profileId, sprintId);
        System.out.println("the points is "+points);

        // by default send the category one
        model.addAttribute("itemMarket",marketItemService.findByCategoryWithPagination(defaultCatNum,0,PAGE_SIZE));
        model.addAttribute("catCount",allActiveCategory.get(0));
        model.addAttribute("totalPages", (int) Math.ceil((double) marketItemService.countByCategory(defaultCatNum) / PAGE_SIZE));
        model.addAttribute("categoryId",defaultCatNum);
        model.addAttribute("sprintId",sprintId);
        model.addAttribute("churchId",userServices.getLogInUserChurch().getId());
        model.addAttribute("meetingId",userServices.getLogInUserMeeting().getId());
        model.addAttribute("userId",userServices.logedInUser().getId());
        model.addAttribute("points",points);


        return "marketHome";
    }
    @GetMapping("/market/category/{id}")
    public String getMarketCategory(Model model, @PathVariable int id, @RequestParam(defaultValue = "0") int page){
        int profileId     = userServices.logedInUser().getProfile().getId();
        int sprintId      = userServices.getActiveSprint().getId();
        Double points     = userPointTransactionService.getTotalPointsByProfileIdAndSprintId(profileId, sprintId);

        model.addAttribute("category" , marketCategoryService.allActiveCategory());
        model.addAttribute("itemMarket",marketItemService.findByCategoryWithPagination(id,page,PAGE_SIZE));
        model.addAttribute("totalPages", (int) Math.ceil((double) marketItemService.countByCategory(id) / PAGE_SIZE));
        model.addAttribute("categoryId",id);
        model.addAttribute("sprintId",sprintId);
        model.addAttribute("churchId",userServices.getLogInUserChurch().getId());
        model.addAttribute("meetingId",userServices.getLogInUserMeeting().getId());
        model.addAttribute("userId",userServices.logedInUser().getId());
        model.addAttribute("points",points);

        return "marketHome";
    }
}
