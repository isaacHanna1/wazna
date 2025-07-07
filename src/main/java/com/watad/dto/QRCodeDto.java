package com.watad.dto;

public class QRCodeDto {

    private String Description ;
    private String valiadFrom;
    private String valiadTo;

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
