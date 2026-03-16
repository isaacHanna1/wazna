package com.watad.dto.attendance;

import com.watad.enumValues.Gender;
import lombok.Data;

import java.time.LocalDate;

@Data
public class AttendanceStatusResponse {

    private int     profileId;
    private String  first_name;
    private String  last_name;
    private Gender  gendre;
    private String  phone;
    private String  email;
    private String  service_class;
    private String  attendance_status;  // "Present" or "Absent"
    private String  scanned_at;       // null if absent
    private String valid_date;
}
