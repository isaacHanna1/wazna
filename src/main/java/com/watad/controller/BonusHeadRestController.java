package com.watad.controller;

import com.watad.entity.BonusHead;
import com.watad.services.BonusHeadService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class BonusHeadRestController {

    private final BonusHeadService bonusHeadService;

    public BonusHeadRestController(BonusHeadService bonusHeadService) {
        this.bonusHeadService = bonusHeadService;
    }

    @GetMapping("/evaluation")
    List<BonusHead> finalBonusByEvaluationType(@RequestParam String evaluationType){
        return bonusHeadService.findByEvaluationType(evaluationType);
    }
}
