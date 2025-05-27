package com.watad.controller;

import com.watad.entity.ServiceStage;
import com.watad.services.ServiceStagesService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController()
@RequestMapping("/api")
public class serviceStageController {

    private final ServiceStagesService serviceStagesService;

    public serviceStageController(ServiceStagesService serviceStagesService) {
        this.serviceStagesService = serviceStagesService;
    }

    @GetMapping("/serviceStage")
    public List<ServiceStage> findAll(){
        return serviceStagesService.findAll();
    }


    @GetMapping("/serviceStage/{id}")
    public ServiceStage findById(@PathVariable int id)
    {
        return serviceStagesService.findById(id);

    }
}
