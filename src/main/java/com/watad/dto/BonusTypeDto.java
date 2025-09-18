package com.watad.dto;

public class BonusTypeDto {

    private int id;
    private String description;
    private int point;

    public BonusTypeDto(int id, String description, int point) {
        this.id = id;
        this.description = description;
        this.point = point;
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
}



