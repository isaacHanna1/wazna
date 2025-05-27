package com.watad.dto;

public class PointsSummaryDTO {


    double points ;
    double balance;
    String redirectURl ; // the url for successful transaction


    public PointsSummaryDTO(double points, double balance ){
        this.points = points;
        this.balance = balance;
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
}
