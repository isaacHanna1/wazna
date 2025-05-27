package com.watad.controller;


import com.watad.services.BonusTypeService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/youth/point")
public class YouthPointsController {

    private final BonusTypeService bonusTypeService;

    public YouthPointsController(BonusTypeService bonusTypeService) {
        this.bonusTypeService = bonusTypeService;
    }

    @GetMapping("/add")
    public String showFormAddBounce(Model model){
        model.addAttribute("bonusType",bonusTypeService.findAll());
        return "addBounce";
    }
}
