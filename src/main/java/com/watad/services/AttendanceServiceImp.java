package com.watad.services;

import com.watad.dao.AttendanceDao;
import com.watad.entity.Attendance;
import org.springframework.stereotype.Service;

@Service
public class AttendanceServiceImp implements AttendanceService{

    private final AttendanceDao attendanceDao;

    public AttendanceServiceImp(AttendanceDao attendanceDao) {
        this.attendanceDao = attendanceDao;
    }

    @Override
    public void save(Attendance attendance) {
        attendanceDao.save(attendance);
    }
}
