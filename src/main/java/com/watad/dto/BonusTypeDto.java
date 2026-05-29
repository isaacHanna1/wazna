package com.watad.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class BonusTypeDto {

    private int id;
    private String description;
    private int point;
    private boolean isActive;
    private LocalDate activeFrom;
    private LocalDate activeTo;
    private boolean physicalAttendanceRequired;


    public BonusTypeDto(int id, String description, int point, boolean isActive,boolean physicalAttendanceRequired, LocalDate activeFrom, LocalDate activeTo) {
        this.id = id;
        this.description = description;
        this.point = point;
        this.isActive = isActive;
        this.physicalAttendanceRequired = physicalAttendanceRequired;
        this.activeFrom = activeFrom;
        this.activeTo = activeTo;
    }

}



