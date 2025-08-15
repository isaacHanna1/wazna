package com.watad.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class PointTransactionSummaryDto {

    private LocalDateTime transactionTime ;
    private String transactionType;
    private String usedFor ;
    private double points ; // added or transfered
    private double current_sum ; // after add the points\
    private double point_before ; // before add points


    public PointTransactionSummaryDto(LocalDateTime transactionTime, String transactionType, String usedFor, double points, double current_sum, double point_before) {
        this.transactionTime = transactionTime;
        this.transactionType = transactionType;
        this.usedFor = usedFor;
        this.points = points;
        this.current_sum = current_sum;
        this.point_before = point_before;
    }

    public LocalDateTime getTransactionTime() {
        return transactionTime;
    }

    public void setTransactionTime(LocalDateTime transactionTime) {
        this.transactionTime = transactionTime;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public String getUsedFor() {
        return usedFor;
    }

    public void setUsedFor(String usedFor) {
        this.usedFor = usedFor;
    }

    public double getPoints() {
        return points;
    }

    public void setPoints(double points) {
        this.points = points;
    }

    public double getCurrent_sum() {
        return current_sum;
    }

    public void setCurrent_sum(double current_sum) {
        this.current_sum = current_sum;
    }

    public double getPoint_before() {
        return point_before;
    }

    public void setPoint_before(double point_before) {
        this.point_before = point_before;
    }
}
