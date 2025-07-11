package com.watad.controller;

import com.watad.dto.ProfileDtlDto;
import com.watad.entity.Profile;
import com.watad.services.ProfileService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class ProfileRestControl {


    private final ProfileService profileService;
    public ProfileRestControl(ProfileService profileService) {
        this.profileService = profileService;
    }
    @GetMapping("/profile")
    public List<ProfileDtlDto> findAll(){
        return profileService.findAll();
    }
    @GetMapping("/profile/search")
    public List<ProfileDtlDto> findAllByPhone(
            @RequestParam String phone
    )
    {
        return profileService.findByUserPhone(phone);
    }


}
