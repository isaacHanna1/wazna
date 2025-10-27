package com.watad.controller;


import com.watad.entity.Profile;
import com.watad.services.DiocesesService;
import com.watad.services.ServiceStagesService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/child")
public class RegisterChildController {
    private final DiocesesService diocesesService;
    private final ServiceStagesService serviceStagesService;

    public RegisterChildController(DiocesesService diocesesService, ServiceStagesService serviceStagesService) {
        this.diocesesService = diocesesService;
        this.serviceStagesService = serviceStagesService;
    }

    @GetMapping("/register")
    public String registerChildView(Model model , Profile profile){
            addDataToModel(model ,profile);
            return "registerChild";
    }


    private void addDataToModel(Model model, Profile profile){
        model.addAttribute("profile",profile);
        model.addAttribute(("dioceses"),diocesesService.findAll());
        model.addAttribute("stages",serviceStagesService.findAll());
        //model.addAttribute("church",churchService.findAll());
        //model.addAttribute("meeting",meetingService.findAll());
    }
}
