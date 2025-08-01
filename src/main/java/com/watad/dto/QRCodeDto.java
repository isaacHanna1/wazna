package com.watad.dto;

public class QRCodeDto {

    private int id ;
    private String Description ;
    private String valiadFrom;
    private String valiadTo;
    private boolean active ;

    public QRCodeDto() {
    }

    public QRCodeDto(int id, boolean active) {
        this.id = id;
        this.active = active;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }


    public String getValiadFrom() {
        return valiadFrom;
    }

    public void setValiadFrom(String valiadFrom) {
        this.valiadFrom = valiadFrom;
    }

    public String getValiadTo() {
        return valiadTo;
    }

    public void setValiadTo(String valiadTo) {
        this.valiadTo = valiadTo;
    }
}
