package com.watad.services;

import com.watad.dto.PointsSummaryDTO;
import com.watad.entity.User;

// in this we save the logic for if the user have valid QR code so save in attendance table
// and add point to user

public interface AttendanceProcessingService {

    PointsSummaryDTO attendanceProcessing(User user , String code);
}
