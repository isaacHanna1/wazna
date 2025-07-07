package com.watad.dto;

public class PointsSummaryDTO {


    double points ;
    double balance;
    int userId ;
    String firstName ;
    String lastName;
    String phone ;

    String redirectURl ; // the url for successful transaction


    public PointsSummaryDTO(double points, double balance ){
        this.points = points;
        this.balance = balance;
    }


    public PointsSummaryDTO(double points, double balance, int userId, String firstName, String lastName, String phone) {
        this.points = points;
        this.balance = balance;
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
    }

    public double getPoints() {
        return points;
    }

    public void setPoints(double points) {
        this.points = points;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public String getRedirectURl() {
        return redirectURl;
    }

    public void setRedirectURl(String redirectURl) {
        this.redirectURl = redirectURl;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
