package com.watad.controller;


import com.watad.dto.PriestDto;
import com.watad.services.PriestService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class PriestRestController {

    private final PriestService priestService;

    public PriestRestController(PriestService priestService) {
        this.priestService = priestService;
    }

    @GetMapping("/periest/{dioceseId}")
    public List<PriestDto> getPriestbyDiocesesId(@PathVariable int dioceseId ){
        return priestService.findByDioceses(dioceseId);
    }

}
