package com.watad.controller.reports;

import com.watad.dto.reports.DailyWaznaReport;
import com.watad.services.SprintDataService;
import com.watad.services.UserServices;
import com.watad.services.YouthServiceClass;
import com.watad.services.reports.WaznaReportServices;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/report")
public class ReportController {

    private final SprintDataService sprintDataService;
    private final UserServices userServices;
    private final WaznaReportServices waznaReportServices;
    private final YouthServiceClass youthServiceClass;

    public ReportController(SprintDataService sprintDataService, UserServices userServices, WaznaReportServices waznaReportServices, YouthServiceClass youthServiceClass) {
        this.sprintDataService   = sprintDataService;
        this.userServices        = userServices;
        this.waznaReportServices = waznaReportServices;
        this.youthServiceClass   = youthServiceClass;
    }

    @GetMapping("/dailyReport")
    public String viewDailyReport(Model model) {
        int meetingId = userServices.getLogInUserMeeting().getId();
        int churchId = userServices.getLogInUserChurch().getId();
        Map<Integer, String> currentClassByStage = youthServiceClass.getCurrentClassByStage();

        model.addAttribute("sprints", sprintDataService.findAll(churchId, meetingId));
        model.addAttribute("today", LocalDate.now());
        model.addAttribute("churchId", churchId);
        model.addAttribute("meetingId", meetingId);
        model.addAttribute("currentClasses",currentClassByStage);

        return "dailyReports";
    }

    @GetMapping("/generateDailyWaznaReport")
    public String generateWaznaReport(
            @RequestParam int sprintId,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startFromDate,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endToDate,
            @RequestParam(defaultValue = "ALL") String profileId,
            @RequestParam(defaultValue = "ALL") String point_source_type,
            @RequestParam(defaultValue = "ALL") String waznaType,
            @RequestParam(defaultValue = "")    String userName,
            @RequestParam(defaultValue = "ALL") String bounce_type_filter,
            @RequestParam(defaultValue = "") String service_class,
            Model model) {

        // Default to today if dates not provided
        if (startFromDate == null) startFromDate = LocalDate.now();
        if (endToDate == null) endToDate = LocalDate.now();

        int meetingId = userServices.getLogInUserMeeting().getId();
        int churchId = userServices.getLogInUserChurch().getId();
        Map<Integer, String> currentClassByStage = youthServiceClass.getCurrentClassByStage();

        List<DailyWaznaReport> reports = waznaReportServices.viewReportOfWaznaAddedToUsers(sprintId, startFromDate, endToDate,profileId, point_source_type, waznaType,bounce_type_filter,service_class);

            model.addAttribute("reports", reports);
            model.addAttribute("sprintId", sprintId);
            model.addAttribute("churchId", churchId);
            model.addAttribute("meetingId", meetingId);
            model.addAttribute("startDate", startFromDate);
            model.addAttribute("endDate", endToDate);
            model.addAttribute("waznaType", waznaType);
            model.addAttribute("point_source_type", point_source_type);
            model.addAttribute("totalRecords", reports.size());
            model.addAttribute("userName", userName);
            model.addAttribute("profileId", profileId);
            model.addAttribute("bounce_type_filter",bounce_type_filter);
            model.addAttribute("currentClasses",currentClassByStage);
            model.addAttribute("service_class",service_class);


            System.out.println("the bounce_type_filter => "+bounce_type_filter);
        // Keep sprints for dropdown
        model.addAttribute("sprints", sprintDataService.findAll(churchId, meetingId));

        return "dailyReports";
    }

}
