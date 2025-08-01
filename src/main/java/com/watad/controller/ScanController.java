package com.watad.controller;


import com.watad.services.UserServices;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ScanController {

    private final  UserServices userServices;

    public ScanController(UserServices userServices) {
        this.userServices = userServices;
    }

    @GetMapping("/scan")
    public String scanPage(Model model){
        model.addAttribute("userId",userServices.logedInUser().getId());
        return "scan";
    }

    @GetMapping("/notifyWithPoints")
    public String pointsNotification(@RequestParam("points") Double points,
                                     @RequestParam("balance") Double balance, Model model) {
        model.addAttribute("points", points);
        model.addAttribute("balance", balance);
        return "pointsNotification";
    }
}
