package com.watad.controller;


import com.watad.entity.*;
import com.watad.exceptions.NotEnoughPointsException;
import com.watad.services.BonusTypeService;
import com.watad.services.UserPointTransactionService;
import com.watad.services.UserServices;
import com.watad.services.YouthRankService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/youth/point")
public class YouthPointsController {

    private final BonusTypeService bonusTypeService;
    private  final UserPointTransactionService upts;
    private final UserServices userServices;
    private final YouthRankService youthRankService;
    private final UserPointTransactionService  userPointTransactionService;

    public YouthPointsController(BonusTypeService bonusTypeService, UserPointTransactionService upts, UserServices userServices, YouthRankService youthRankService, UserPointTransactionService userPointTransactionService) {
        this.bonusTypeService = bonusTypeService;
        this.upts = upts;
        this.userServices = userServices;
        this.youthRankService = youthRankService;
        this.userPointTransactionService = userPointTransactionService;
    }

    @GetMapping("/add")
    public String showFormAddBounce(Model model){
        int id      = userServices.logedInUser().getId();
        model.addAttribute("bonusType",bonusTypeService.findAll());
        model.addAttribute("user_id",id);
        return "addBounce";
    }

    @GetMapping("/transfer")
    public String transferPoint(Model model){
        List<User> superUSers = userServices.findByRole(3); // get user have Super role;
        model.addAttribute("SuperUser",superUSers);
        model.addAttribute("logedInUser",userServices.logedInUser().getId());
        return  "TransferWazna";
    }

    @PostMapping("/transfer")
    public String transferPoint(@RequestParam int fromUserId,
                                @RequestParam int toUserId,
                                @RequestParam double point,
                                @RequestParam String reason,
                                RedirectAttributes redirectAttributes) {
        String message = "We succeed in transferring wazna";
        String type = "success";

        try {
            boolean result = upts.transferPoint(fromUserId, toUserId, point, reason);
        } catch (NotEnoughPointsException ex) {
            message = "You don't have enough wazna points to complete this transfer.";
            type = "error";
        }

        redirectAttributes.addFlashAttribute("message", message);
        redirectAttributes.addFlashAttribute("type", type);
        return "redirect:/youth/point/transfer";
    }

    // Start view for point transaction summary
    @GetMapping("/details")
    public String transactionSummary(Model model){
        double youthPoint = youthRankService.getYouthPoint();
        String first_name = userServices.getLogedInUserProfile().getFirstName();
        model.addAttribute("balance",youthPoint);
        model.addAttribute("fName",first_name +"`s Transaction Details");
        model.addAttribute("transactionList",userPointTransactionService.getSummaryOfPoints());
        return "transactionHistory";
    }
    // End view for point transaction summary

}
