package com.watad.controller;

import com.watad.dto.ProfileDtlDto;
import com.watad.entity.Profile;
import com.watad.entity.User;
import com.watad.services.ProfileService;
import com.watad.services.UserServices;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ProfileController {

    private final ProfileService profileService;
    private final UserServices userServices;
    public ProfileController(ProfileService profileService , UserServices userServices) {
        this.profileService = profileService;
        this.userServices = userServices;
    }

    @GetMapping("/profile")
    public String viewProfile(Model model){
        ProfileDtlDto dto = profileService.getProfileById();
        model.addAttribute("profileDto",dto);
        return "profile";
    }
}
