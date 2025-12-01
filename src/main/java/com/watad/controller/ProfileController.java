package com.watad.controller;

import com.watad.dto.ProfileDtlDto;
import com.watad.dto.UserCountsDto;
import com.watad.entity.Profile;
import com.watad.entity.Role;
import com.watad.entity.SystemConfig;
import com.watad.entity.User;
import com.watad.exceptions.PhomeNumberAlreadyException;
import com.watad.exceptions.ProfileException;
import com.watad.services.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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
    private final PriestService priestService;
    private final SystemConfigService systemConfig;
    private final YouthServiceClass youthServiceClass;
    private final RoleService roleService;


    public ProfileController(ServiceStagesService serviceStagesService, ChurchService churchService, MeetingService meetingService, ProfileService profileService, UserServices userServices, DiocesesService diocesesService, PriestService priestService, SystemConfigService systemConfig, YouthServiceClass youthServiceClass, RoleService roleService) {
        this.serviceStagesService = serviceStagesService;
        this.churchService = churchService;
        this.meetingService = meetingService;
        this.profileService = profileService;
        this.userServices = userServices;
        this.diocesesService = diocesesService;
        this.priestService = priestService;
        this.systemConfig = systemConfig;
        this.youthServiceClass = youthServiceClass;
        this.roleService = roleService;
    }

    @GetMapping("/profile")
    public String viewProfile(Model model){
        ProfileDtlDto dto = profileService.getProfileById();
        int logedInProfileId = userServices.getLogedInUserProfile().getId();
        boolean isChildRegisterionExists       = systemConfig.getSystemCongigValueByKey("child_req");

        model.addAttribute("profileDto",dto);
        model.addAttribute("logedInProfileId" , logedInProfileId);
        model.addAttribute("isChildRegisterionExists" , isChildRegisterionExists);

        return "profile";
    }
    @GetMapping("/profile/{userId}")
    public String viewSpecificProfile(Model model , @PathVariable int userId){
        ProfileDtlDto dto = profileService.getProfileData(userId);
        model.addAttribute("profileDto",dto);
        return "profile";
    }

    @PreAuthorize(
            "hasAnyRole('SERVER', 'CLASS_LEADER', 'TREASURER', 'SERVICE_LEADER', 'SUPER') " +
                    "or @ProfileService.isUserProfileOwner(#id, authentication.principal.id)"
    )
    @GetMapping("/editProfile/{id}")
    public String editProfile(Model model , @PathVariable int id , HttpServletRequest request) throws ProfileException {
        Profile profile = profileService.getEditableProfile(id,request);
        addDataToModel(model,profile);
        return "editProfile";
    }


    private void addDataToModel(Model model,Profile profile){
        model.addAttribute("profile",profile);
        model.addAttribute("stages",serviceStagesService.findAll());
        model.addAttribute(("dioceses"),diocesesService.findAll());
        int diocesesId = profile.getDioceses().getId();
        int churchId  = profile.getChurch().getId();
        model.addAttribute("church",churchService.findByDioceseId(diocesesId));
        model.addAttribute("meeting",meetingService.findByChurchId(churchId));
        model.addAttribute("priests",priestService.findByDioceses(profile.getDioceses().getId()));
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
            int userId = profileService.getProfileById(id).getUser().getId();
        return "redirect:/profile/"+userId;
    }

    // profile management for active or in active users
    @GetMapping("/profileManage")
    public String profileManagement(
            Model model,
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "30") int pageSize,
            @RequestParam(defaultValue = "false") String status,
            @RequestParam(defaultValue = "All") String gender,
            @RequestParam(required = false, defaultValue = "0") Integer searchProfileId,
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "All") String serviceClass,
            @RequestParam(defaultValue = "All") String searchRole) {

        // 1. Retrieve counts (summary data)
        UserCountsDto dto = userServices.getCountsInMeeting();

        // 2. Retrieve paginated profiles
        List<ProfileDtlDto> profileDtlDtos = profileService.findAllByFilterPaginated(
                searchProfileId, status, gender, pageNum, pageSize, serviceClass ,searchRole
        );

        //  3. Get total number of pages for pagination
        int numOfPages = profileService.getTotalPagesByFilter(
                status, gender, pageSize, searchProfileId, serviceClass,searchRole
        );

        //  4. Context info about the logged-in user
        int churchId = userServices.getLogInUserChurch().getId();
        int meetingId = userServices.getLogInUserMeeting().getId();
        int serviceStageId = userServices.getLogedInUserProfile().getServiceStage().getId();

        //  5. System configuration (safe boolean flag)
        boolean isChildRegisterionExists = systemConfig.getSystemCongigValueByKey("child_req");

        //  6. Add data to model (grouped logically)
        // --- Profile Data ---
        model.addAttribute("profiles", profileDtlDtos);
        model.addAttribute("searchProfileId", searchProfileId);
        model.addAttribute("keyword", keyword);

        // --- Summary Counts ---
        model.addAttribute("totalUser", dto.getTotalProfiles());
        model.addAttribute("activeUser", dto.getActiveProfiles());
        model.addAttribute("inactiveUser", dto.getInactiveProfiles());

        // --- Pagination ---
        model.addAttribute("numOfPages", numOfPages);
        model.addAttribute("currentPage", pageNum);
        model.addAttribute("pageSize", pageSize);

        // --- Filters ---
        model.addAttribute("status", status);
        model.addAttribute("gender", gender);
        model.addAttribute("serviceClass", youthServiceClass.getServicesClassByStage(serviceStageId));
        model.addAttribute("searchClass", serviceClass); // selected class value
        model.addAttribute("roleList",roleService.findRolesWithArabDesc());
        model.addAttribute("searchRole",searchRole);

        // --- Context ---
        model.addAttribute("churchId", churchId);
        model.addAttribute("meetingId", meetingId);
        model.addAttribute("isChildRegisterionExists", isChildRegisterionExists);

        return "profileManagement";
    }

}
