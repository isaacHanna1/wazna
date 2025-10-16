package com.watad.dto;

import java.time.LocalDate;

public class BonusTypeDto {

    private int id;
    private String description;
    private int point;
    private boolean isActive;
    private LocalDate activeFrom;
    private LocalDate activeTo;


    public BonusTypeDto(int id, String description, int point) {
        this.id = id;
        this.description = description;
        this.point = point;
    }

    public BonusTypeDto(int id, String description, int point, boolean isActive, LocalDate activeFrom, LocalDate activeTo) {
        this.id = id;
        this.description = description;
        this.point = point;
        this.isActive = isActive;
        this.activeFrom = activeFrom;
        this.activeTo = activeTo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getPoint() {
        return point;
    }

    public void setPoint(int point) {
        this.point = point;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public LocalDate getActiveFrom() {
        return activeFrom;
    }

    public void setActiveFrom(LocalDate activeFrom) {
        this.activeFrom = activeFrom;
    }

    public LocalDate getActiveTo() {
        return activeTo;
    }

    public void setActiveTo(LocalDate activeTo) {
        this.activeTo = activeTo;
    }
}



