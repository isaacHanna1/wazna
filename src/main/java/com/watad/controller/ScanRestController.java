package com.watad.controller;

import com.watad.dto.PointsSummaryDTO;
import com.watad.entity.User;
import com.watad.services.AttendanceProcessingService;
import com.watad.services.AttendanceProcessingServiceImp;
import com.watad.services.QrCodeService;
import com.watad.services.UserServices;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api")
public class ScanRestController {

    private final QrCodeService qrCodeService;
    private final AttendanceProcessingService processingService ;
    private final UserServices userServices;


    public ScanRestController(QrCodeService qrCodeService , AttendanceProcessingService processingService , UserServices userServices) {
        this.qrCodeService = qrCodeService;
        this.processingService = processingService;
        this.userServices = userServices;
    }
    @GetMapping({"/scanner/{code}", "/scanner/{code}/{userId}"})
    public PointsSummaryDTO checkCode(@PathVariable String code , @PathVariable(required = false) Integer userId){
        User user ;
        if(userId != null){
             user = userServices.findUserById(userId);
        }else {
            user  = userServices.logedInUser();
        }
        PointsSummaryDTO pointsSummaryDTO =  processingService.attendanceProcessing(user,code);
        pointsSummaryDTO.setRedirectURl("/notifyWithPoints");
        return pointsSummaryDTO;
    }

}
