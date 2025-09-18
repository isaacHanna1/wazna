package com.watad.controller;

import com.watad.dto.ProfileDtlDto;
import com.watad.dto.UserCountsDto;
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
import java.util.List;

@Controller
public class ProfileController {

    private final ServiceStagesService serviceStagesService;
    private final ChurchService churchService;
    private final MeetingService meetingService;
    private final ProfileService profileService;
    private final UserServices userServices;
    private final DiocesesService diocesesService;

    public ProfileController(ServiceStagesService serviceStagesService, ChurchService churchService, MeetingService meetingService, ProfileService profileService, UserServices userServices , DiocesesService diocesesService) {
        this.serviceStagesService = serviceStagesService;
        this.churchService = churchService;
        this.meetingService = meetingService;
        this.profileService = profileService;
        this.userServices = userServices;
        this.diocesesService = diocesesService;
    }

    @GetMapping("/profile")
    public String viewProfile(Model model){
        ProfileDtlDto dto = profileService.getProfileById();
        model.addAttribute("profileDto",dto);
        int logedInProfileId = userServices.getLogedInUserProfile().getId();
        model.addAttribute("logedInProfileId" , logedInProfileId);
        return "profile";
    }
    @GetMapping("/profile/{userId}")
    public String viewSpecificProfile(Model model , @PathVariable int userId){
        ProfileDtlDto dto = profileService.getProfileData(userId);
        model.addAttribute("profileDto",dto);

        return "profile";
    }

    @GetMapping("/editProfile/{id}")
    public String editProfile(Model model , @PathVariable int id ){
        Profile profile = profileService.getProfileById(id);
        addDataToModel(model,profile);
        model.addAttribute(("dioceses"),diocesesService.findAll());
        return "editProfile";
    }

    private void addDataToModel(Model model,Profile profile){
        model.addAttribute("profile",profile);
        model.addAttribute("stages",serviceStagesService.findAll());
        model.addAttribute("church",churchService.findAll());
        model.addAttribute("meeting",meetingService.findAll());
        model.addAttribute(("dioceses"),diocesesService.findAll());
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



    // profile management for active or in active users
    @GetMapping("/profileManage")
    public String profileManagement(Model model
                    ,@RequestParam int pageNum, @RequestParam int pageSize
                    ,@RequestParam String status , @RequestParam String gender , @RequestParam(required = false) Integer  searchProfileId
                    ,@RequestParam(required = false) String keyword){

        if (searchProfileId == null) {
            searchProfileId = 0;
        }
        UserCountsDto dto                      = userServices.getCountsInMeeting();
        List<ProfileDtlDto>     profileDtlDtos = profileService.findAllByFilterPaginated(searchProfileId,status,gender,pageNum,pageSize);
        Long totalUser                         = dto.getTotalProfiles();
        Long activeUser                        = dto.getActiveProfiles();
        Long inactiveUser                      = dto.getInactiveProfiles();
        int  numOfPages                        = profileService.getTotalPagesByFilter(status,gender,pageSize,searchProfileId);
        int churchId                           = userServices.getLogInUserChurch().getId();
        int meetingId                          = userServices.getLogInUserMeeting().getId();

        model.addAttribute("totalUser",totalUser);
        model.addAttribute("inactiveUser",inactiveUser);
        model.addAttribute("activeUser",activeUser);
        model.addAttribute("profiles",profileDtlDtos);
        model.addAttribute("status",status);
        model.addAttribute("gender",gender);
        model.addAttribute("numOfPages",numOfPages);
        model.addAttribute("currentPage", pageNum);
        model.addAttribute("pageSize",pageSize);
        model.addAttribute("churchId",churchId);
        model.addAttribute("meetingId",meetingId);
        model.addAttribute("keyword",keyword);
        model.addAttribute("searchProfileId",searchProfileId);

        return "profileManagement";
    }


}
