package com.watad.dto.response;

public class YouthRankDto {

    private int    rank;
    private String firstName;
    private String lastName;
    private double point ;
    private String photoPath;
    private String classService;


    public YouthRankDto(int rank, String firstName, String lastName, double point) {
        this.rank = rank;
        this.firstName = firstName;
        this.lastName = lastName;
        this.point = point;
    }

    public YouthRankDto(int rank, String firstName, String lastName, double point, String photoPath, String classService) {
        this.rank = rank;
        this.firstName = firstName;
        this.lastName = lastName;
        this.point = point;
        this.photoPath = photoPath;
        this.classService = classService;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public double getPoint() {
        return point;
    }

    public void setPoint(double point) {
        this.point = point;
    }

    public String getPhotoPath() {
        return photoPath;
    }

    public void setPhotoPath(String photoPath) {
        this.photoPath = photoPath;
    }

    public String getClassService() {
        return classService;
    }

    public void setClassService(String classService) {
        this.classService = classService;
    }
}
