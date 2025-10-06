package com.watad.entity;


import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;

@Entity
@Table(name = "attendance")
public class Attendance {

    @EmbeddedId
    private AttendanceId attendanceId;

    @Column(name = "scanned_at")
    private LocalTime scannedAt = LocalTime.now(ZoneId.of("Africa/Cairo"));


    public Attendance() {
    }

    public AttendanceId getAttendanceId() {
        return attendanceId;
    }

    public void setAttendanceId(AttendanceId attendanceId) {
        this.attendanceId = attendanceId;
    }


    public LocalTime getScannedAt() {
        return scannedAt;
    }

    public void setScannedAt(LocalTime scannedAt) {
        this.scannedAt = scannedAt;
    }
}
