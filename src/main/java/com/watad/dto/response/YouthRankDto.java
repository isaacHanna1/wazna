package com.watad.dto.response;

public class YouthRankDto {

    private int rank;
    private String firstName;
    private String lastName;
    private double point ;


    public YouthRankDto(int rank, String firstName, String lastName, double point) {
        this.rank = rank;
        this.firstName = firstName;
        this.lastName = lastName;
        this.point = point;
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
}
