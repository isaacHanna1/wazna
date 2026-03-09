package com.watad.dto.qrMeeting;

import com.watad.dto.bonusType.BonusTypeResponse;
import com.watad.dto.church.ChurchResponse;
import com.watad.dto.meeting.MeetingResponse;
import com.watad.entity.BonusType;
import com.watad.entity.Church;
import com.watad.entity.Meetings;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@NoArgsConstructor
public class QrMeetingDtoResponse {

    private int id;
    private String code;
    private LocalDate validDate;
    private LocalTime validStart;
    private LocalTime validEnd;
    private String description;
    private boolean active;
    private BonusTypeResponse bonusType;

}
