package com.watad.controller.reports;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Controller
@RequestMapping("report")
public class AttendanceController {

    @GetMapping("/attendance")
    public String getView(){
        return "attendanceReport";
    }
}
