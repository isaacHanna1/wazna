package com.watad.controller;

import com.watad.dto.attendance.AttendanceStatusResponse;
import com.watad.services.AttendanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AttendanceRestController {

    private final AttendanceService attendanceService;
    @GetMapping("/attendance/status")
    public ResponseEntity<List<AttendanceStatusResponse>> getAttendanceResponse(@RequestParam("qr_code_id") int qrId
            , @RequestParam(value = "service_class"
                    ,required = false) String service_class
                    ,@RequestParam(required = false) String role){
        List<AttendanceStatusResponse> attendanceResponse = attendanceService.getAttendanceResponse(qrId, service_class, role);
        return  ResponseEntity.ok(attendanceResponse);
    }
}
