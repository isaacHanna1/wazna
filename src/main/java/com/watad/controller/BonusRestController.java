package com.watad.controller;

import com.watad.dto.bonusType.BonusTypeResponse;
import com.watad.services.BonusTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/bonus-types")
@RequiredArgsConstructor
public class BonusRestController {

    private final BonusTypeService bonusTypeService;


    @GetMapping
    public ResponseEntity<List<BonusTypeResponse>> getActiveBonusType(){
        List<BonusTypeResponse> bonusTypes  = bonusTypeService.findAll("PO",true);
            return  ResponseEntity.ok(bonusTypes);
    }

}
