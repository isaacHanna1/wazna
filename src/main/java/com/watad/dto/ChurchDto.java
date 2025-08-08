package com.watad.dto;

public class ChurchDto {

    private int id ;
    private String churchName;

    public ChurchDto(int id, String churchName) {
        this.id = id;
        this.churchName = churchName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getChurchName() {
        return churchName;
    }

    public void setChurchName(String churchName) {
        this.churchName = churchName;
    }
}
