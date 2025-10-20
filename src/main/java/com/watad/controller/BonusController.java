package com.watad.controller;

import com.watad.entity.BonusType;
import com.watad.services.BonusHeadService;
import com.watad.services.BonusTypeService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/bonus")
public class BonusController {


    private final BonusHeadService bonusHeadService;
    private final BonusTypeService bonusTypeService;

    public BonusController(BonusHeadService bonusHeadService, BonusTypeService bonusTypeService) {
        this.bonusHeadService = bonusHeadService;
        this.bonusTypeService = bonusTypeService;
    }

    @GetMapping("/create")
    public String bonusView(Model model){
        model.addAttribute("bonus",new BonusType());
        model.addAttribute("bonusHead",bonusHeadService.findAllBonusService());
        return "createBonus";
    }

    @PostMapping("/create")
    public String createBonus(Model model,@ModelAttribute("bonus") BonusType bonusType){
        bonusTypeService.createBonusType(bonusType);
        return "redirect:/bonus/all";
    }

    @GetMapping("/all")
    public String viewAll(Model model){
        model.addAttribute("bonus",bonusTypeService.findAll());
        return "allBonus";
    }

    @GetMapping("/find")
    public String findByDesc(Model model, @RequestParam String desc){
        model.addAttribute("bonus",bonusTypeService.findByDesc(desc));
        return "allBonus";
    }

    @GetMapping("/find/{id}")
    public String findByDesc(Model model, @PathVariable int id ){
        model.addAttribute("bonus",bonusTypeService.findById(id));
        model.addAttribute("bonusHead",bonusHeadService.findAllBonusService());
        return "editBonus";
    }
    @PostMapping("/update")
    public String update(Model model,@ModelAttribute("bonus") BonusType bonusType){
        bonusTypeService.updateBonusType(bonusType);
        model.addAttribute("bonus",bonusTypeService.findAll());
        return "allBonus";
    }


}
