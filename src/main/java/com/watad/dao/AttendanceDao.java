package com.watad.dao;

import com.watad.dto.attendance.AttendanceStatusResponse;
import com.watad.entity.Attendance;

import java.util.List;

public interface AttendanceDao {

   void save(Attendance attendance);
   List<AttendanceStatusResponse> getAttendanceResponse( int churchId , int meetingId ,int qrCodeId, String serviceClass,String role);
}
