package com.watad.controller;


import com.watad.entity.*;
import com.watad.exceptions.NotEnoughPointsException;
import com.watad.services.BonusTypeService;
import com.watad.services.UserPointTransactionService;
import com.watad.services.UserServices;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/youth/point")
public class YouthPointsController {

    private final BonusTypeService bonusTypeService;
    private  final UserPointTransactionService upts;
    private final UserServices userServices;

    public YouthPointsController(BonusTypeService bonusTypeService, UserPointTransactionService upts, UserServices userServices) {
        this.bonusTypeService = bonusTypeService;
        this.upts = upts;
        this.userServices = userServices;
    }

    @GetMapping("/add")
    public String showFormAddBounce(Model model){
        model.addAttribute("bonusType",bonusTypeService.findAll());
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
                                Model model) {
        String message    = "We suceed transfer wazna ";
        String type       = "pass";



        try {
            boolean result = upts.transferPoint(fromUserId, toUserId, point, reason);
        } catch (NotEnoughPointsException ex){
            message      = "You don't have enough wazna points to complete this transfer.";
            type         = "error";
        }
        model.addAttribute("message", message);
        model.addAttribute("type", type);
        return  "TransferWazna";
    }

}
