package com.watad.controller;

import com.watad.dto.BonusTypeDto;
import com.watad.entity.BonusType;
import com.watad.services.BonusTypeService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class BonusTypeRestController {

    private final BonusTypeService bonusTypeService;

    public BonusTypeRestController(BonusTypeService bonusTypeService) {
        this.bonusTypeService = bonusTypeService;
    }

    @GetMapping("/bonusType")
    public List<BonusTypeDto> getBonusType(@RequestParam(defaultValue = "All") String evaluationType){
        return  bonusTypeService.findAll(evaluationType);
    }
}
