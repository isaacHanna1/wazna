package com.watad.controller.marketDashboard;

import com.watad.dto.sprint.SprintResponse;
import com.watad.entity.SprintData;
import com.watad.services.SprintDataService;
import com.watad.services.UserServices;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/dashboard")
@RequiredArgsConstructor
public class marketDashBoardController {

    private final SprintDataService sprintDataService;
    private final UserServices userServices;
    @GetMapping()
    public String dashBoardView(Model model){
        int churchId   = userServices.getLogInUserChurch().getId();
        int meetingId  = userServices.getLogInUserMeeting().getId();
        List<SprintResponse> allSprints = sprintDataService.findAllDto(churchId,meetingId);
        model.addAttribute("sprints",allSprints);
        return "Marketdashboard";
    }
}
