package com.watad.controller;

import com.watad.entity.Profile;
import com.watad.exceptions.PhomeNumberAlreadyException;
import com.watad.services.*;
import jakarta.validation.Valid;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Controller
public class AuthController {


    private final ProfileService profileService;
    private final ServiceStagesService serviceStagesService;
    private final ChurchService churchService;
    private final MeetingService meetingService;
    private final DiocesesService diocesesService;
    public AuthController(ProfileService profileService , ServiceStagesService serviceStagesService , ChurchService churchService , MeetingService meetingService , DiocesesService diocesesService) {
        this.profileService             = profileService;
        this.serviceStagesService       = serviceStagesService;
        this.churchService              = churchService;
        this.meetingService             = meetingService;
        this.diocesesService            = diocesesService;
    }

    @GetMapping("/sign-in")
    public String showLoginForm(){
        return "sign-in";
    }
    @PostMapping("/login")
    public String loginLogic(){
        System.out.println("I`m called there ");
        return "";
    }

    @GetMapping("/register")
    public String showRegisterForm(Model model){
        Profile profile     = new Profile();
        addDataToModel(model,profile);
        return "register";
    }
    @PostMapping("/register")
    public String registerUser(
            @Valid @ModelAttribute("profile") Profile profile,
            BindingResult result,
            Model model, @RequestParam("image") MultipartFile image

            ) {
        if (result.hasErrors()) {
            addDataToModel(model,profile);
            return "register";
        }
        if (image == null || image.isEmpty()) {
            addDataToModel(model,profile);
            result.rejectValue("profileImagePath", "error.profile", "Image is required");
            return "register";
        }

        try {
            addDataToModel(model,profile);
            profileService.saveProfile(profile, image);
        } catch (IOException io) {
            addDataToModel(model,profile);
            model.addAttribute("error", "Failed to upload image");
            return "register";
        } catch (PhomeNumberAlreadyException ex){
            addDataToModel(model, profile);
            result.rejectValue("phone", "error.phone", ex.getMessage());
            return "register";
        }

        return "redirect:/sign-in";
    }

    @GetMapping("/accessDenied")
    public  String accessDenied(){
        return "accessDenied";
    }
    private void addDataToModel(Model model,Profile profile){
        model.addAttribute("profile",profile);
        model.addAttribute("stages",serviceStagesService.findAll());
        model.addAttribute("church",churchService.findAll());
        model.addAttribute("meeting",meetingService.findAll());
        model.addAttribute(("dioceses"),diocesesService.findAll());
    }

    @InitBinder
    public void initBinder(WebDataBinder dataBinder) {
        StringTrimmerEditor stringTrimmerEditor = new StringTrimmerEditor(true);
        dataBinder.registerCustomEditor(String.class, stringTrimmerEditor);
    }
}
