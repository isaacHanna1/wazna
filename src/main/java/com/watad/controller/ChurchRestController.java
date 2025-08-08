package com.watad.controller;

import com.watad.dto.ChurchDto;
import com.watad.entity.Church;
import com.watad.services.ChurchService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class ChurchRestController {

    private  final ChurchService churchService;

    public ChurchRestController(ChurchService churchService) {
        this.churchService = churchService;
    }

    @GetMapping("/church/{dioceseId}")
    public List<ChurchDto> findByDiocesesId(@PathVariable int dioceseId){
        return  churchService.findByDioceseId(dioceseId);
    }

}
