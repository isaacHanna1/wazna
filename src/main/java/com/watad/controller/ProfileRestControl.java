package com.watad.controller;

import com.watad.dto.ProfileDtlDto;
import com.watad.entity.Profile;
import com.watad.services.ProfileService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
        return profileService.findByUserPhoneOrUserName(phone);
    }

    @GetMapping("/profile/{keyword}")
    public  List<ProfileDtlDto> searchByNameOrPhone(@PathVariable String keyword , @RequestParam int churchId , @RequestParam int meetingId){
        return  profileService.findProfileByNameOrPhone(keyword, churchId, meetingId);
    }

    @DeleteMapping("/profile/delete/{profileId}")
    public ResponseEntity<String> deleteProfile(@PathVariable  int profileId){
        try {
            profileService.deleteProfileById(profileId);
            return  ResponseEntity.ok("Profile Deleted");
        } catch (RuntimeException e) {
            System.out.println("When deleting data -> "+e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Internal Error Profile Deleting");
        }
    }

}
