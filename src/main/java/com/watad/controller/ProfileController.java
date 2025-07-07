package com.watad.controller;

import com.watad.dto.ProfileDtlDto;
import com.watad.entity.Profile;
import com.watad.entity.User;
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
public class ProfileController {

    private final ServiceStagesService serviceStagesService;
    private final ChurchService churchService;
    private final MeetingService meetingService;
    private final ProfileService profileService;
    private final UserServices userServices;

    public ProfileController(ServiceStagesService serviceStagesService, ChurchService churchService, MeetingService meetingService, ProfileService profileService, UserServices userServices) {
        this.serviceStagesService = serviceStagesService;
        this.churchService = churchService;
        this.meetingService = meetingService;
        this.profileService = profileService;
        this.userServices = userServices;
    }

    @GetMapping("/profile")
    public String viewProfile(Model model){
        ProfileDtlDto dto = profileService.getProfileById();
        model.addAttribute("profileDto",dto);
        return "profile";
    }

    @GetMapping("/editProfile/{id}")
    public String editProfile(Model model , @PathVariable int id ){
        Profile profile = profileService.getProfileById(id);
        addDataToModel(model,profile);
        return "editProfile";
    }

    private void addDataToModel(Model model,Profile profile){
        model.addAttribute("profile",profile);
        model.addAttribute("stages",serviceStagesService.findAll());
        model.addAttribute("church",churchService.findAll());
        model.addAttribute("meeting",meetingService.findAll());
    }
    @PostMapping("/editProfile/{id}")
    public String registerUser(
            @Valid @ModelAttribute("profile") Profile profile,
            BindingResult result,
            Model model, @RequestParam("image") MultipartFile image,@PathVariable int id
    ) {
        if (result.hasErrors()) {
            addDataToModel(model,profile);
            return "editProfile";
        }


        try {
            addDataToModel(model,profile);
            profileService.editPrfofile(profile, image ,id);
        } catch (IOException io) {
            addDataToModel(model,profile);
            model.addAttribute("error", "Failed to upload image");
            return "editProfile";
        } catch (PhomeNumberAlreadyException ex){
            addDataToModel(model, profile);
            result.rejectValue("phone", "error.phone", ex.getMessage());
            return "editProfile";
        }

        return "redirect:/profile";
    }



}
