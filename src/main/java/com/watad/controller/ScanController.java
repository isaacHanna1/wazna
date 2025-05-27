package com.watad.controller;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ScanController {


    @GetMapping("/scan")
    public String scanPage(){
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
