package com.watad.controller;


import com.watad.entity.Profile;
import com.watad.exceptions.PhomeNumberAlreadyException;
import com.watad.services.*;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Controller
@RequestMapping("/child")
public class RegisterChildController {
    private final DiocesesService diocesesService;
    private final ServiceStagesService serviceStagesService;
    private final RegisterChildServices registerChildServices;
    private final ProfileService profileService ;
    private final ChurchService churchService;
    private final MeetingService meetingService;
    private final PriestService priestService;
    private final UserServices userServices;


    public RegisterChildController(DiocesesService diocesesService, ServiceStagesService serviceStagesService, RegisterChildServices registerChildServices, ProfileService profileService, ChurchService churchService, MeetingService meetingService, PriestService priestService, UserServices userServices) {
        this.diocesesService = diocesesService;
        this.serviceStagesService = serviceStagesService;
        this.registerChildServices = registerChildServices;
        this.profileService = profileService;
        this.churchService = churchService;
        this.meetingService = meetingService;
        this.priestService = priestService;
        this.userServices = userServices;
    }

    @GetMapping("/register")
    public String registerChildView(Model model , Profile profile){
            addDataToModel(model ,profile);
            return "registerChild";
    }

    @PostMapping("/register")
    public String registerUser(@Valid @ModelAttribute("profile") Profile profile,Model model, @RequestParam("image") MultipartFile image) {

        try{
        registerChildServices.registerChild(profile,image);
        } catch (IOException io) {
            addDataToModel(model,profile);
            model.addAttribute("error", "Failed to upload image");
            return "register";
        }
        return "redirect:/child/register";
    }

    @GetMapping("/EditProfile/{id}")
    public String editChidProfile(@PathVariable  int id , Model model){
            Profile profile =   profileService.getProfileById(id);
            System.out.println("th id id -> "+id);
            System.out.println("the services class is "+profile.getServiceClass());
            model.addAttribute("profile",profile);
            addDataToModel(model,profile);
        model.addAttribute("priests",priestService.findByDioceses(profile.getDioceses().getId()));
        return "registerChildEdit";
    }
    @PostMapping("/editProfile")
    public String editChidProfile( @ModelAttribute("profile") Profile profile,Model model, @RequestParam("image") MultipartFile image) {
        try {
            System.out.println("when update the id is "+profile.getId());
            profileService.editProfileForChild(profile, image , profile.getId());
        } catch (IOException io) {
            addDataToModel(model,profile);
            model.addAttribute("error", "Failed to upload image");
            return "editProfile";
        }
        return "redirect:/child/EditProfile/"+profile.getId();
    }
    private void addDataToModel(Model model, Profile profile){
        Profile currentProfile = userServices.getLogedInUserProfile();

        profile.setDioceses(currentProfile.getDioceses());
        profile.setServiceStage(currentProfile.getServiceStage());
        profile.setChurch(currentProfile.getChurch());
        profile.setMeetings(currentProfile.getMeetings());
        profile.setServiceStage(currentProfile.getServiceStage());
        model.addAttribute("profile",profile);
        model.addAttribute("dioceses",diocesesService.findById(currentProfile.getDioceses().getId()));
        model.addAttribute("stages",serviceStagesService.findById(currentProfile.getServiceStage().getId()));
        model.addAttribute("church",currentProfile.getChurch());
        model.addAttribute("meeting",currentProfile.getMeetings());
        model.addAttribute("periests",priestService.findByDioceses(currentProfile.getDioceses().getId()));
    }
}
