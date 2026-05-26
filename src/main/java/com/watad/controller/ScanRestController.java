package com.watad.controller;

import com.watad.Common.TimeUtil;
import com.watad.dto.PointsSummaryDTO;
import com.watad.entity.User;
import com.watad.services.AttendanceProcessingService;
import com.watad.services.QrCodeService;
import com.watad.services.UserServices;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Slf4j
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ScanRestController {

    private final QrCodeService qrCodeService;
    private final AttendanceProcessingService processingService ;
    private final UserServices userServices;
    private final TimeUtil timeUtil;

    @GetMapping({"/scanner/{code}", "/scanner/{code}/{userId}"})
    public PointsSummaryDTO attendanceManual(
            @PathVariable String code,
            @PathVariable(required = false) Integer userId,
            @RequestParam(required = false)
            @DateTimeFormat(pattern = "HH:mm")
                LocalTime time ,
            @RequestParam(required = false) LocalDate theTakenDate
    ) {
        User user = (userId != null)
                ? userServices.findUserById(userId)
                : userServices.logedInUser();

        time = (time != null) ? time   : timeUtil.now_localTime();
        theTakenDate = (theTakenDate !=null)? theTakenDate : timeUtil.now_localDate();

        log.info("the time is {}",time);
        PointsSummaryDTO pointsSummaryDTO =  processingService.attendanceProcessing(user,code,theTakenDate,time);
        pointsSummaryDTO.setRedirectURl("/notifyWithPoints");
        return pointsSummaryDTO;
    }

}
