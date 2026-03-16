package com.watad.services;

import com.watad.dao.AttendanceDao;
import com.watad.dto.attendance.AttendanceStatusResponse;
import com.watad.entity.Attendance;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AttendanceServiceImp implements AttendanceService{

    private final AttendanceDao attendanceDao;
    private final UserServices userServices;
    @Override
    public void save(Attendance attendance) {
        attendanceDao.save(attendance);
    }

    @Override
    public List<AttendanceStatusResponse> getAttendanceResponse(int qrCodeId, String serviceClass,String role) {

        int churchId    = userServices.getLogInUserChurch().getId();
        int meetingId   = userServices.getLogInUserMeeting().getId();
        return attendanceDao.getAttendanceResponse(churchId,meetingId,qrCodeId,serviceClass,role);
    }
}
