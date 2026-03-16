package com.watad.services;

import com.watad.dto.attendance.AttendanceStatusResponse;
import com.watad.entity.Attendance;

import java.util.List;

public interface AttendanceService {

    void save(Attendance attendance);
    List<AttendanceStatusResponse> getAttendanceResponse(int qrCodeId, String serviceClass,String role);
}
