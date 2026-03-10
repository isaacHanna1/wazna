package com.watad.dto.qrMeeting;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@NoArgsConstructor
@ToString
public class QrMeetingDtoRequest {

    private int id;
    private String code;
    private String description;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate validDate;
    @DateTimeFormat(pattern = "HH:mm")
    private LocalTime validStart;
    @DateTimeFormat(pattern = "HH:mm")
    private LocalTime validEnd;
    private boolean active;
    private int bonusTypeId;
}