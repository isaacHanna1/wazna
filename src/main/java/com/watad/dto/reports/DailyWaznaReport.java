package com.watad.dto.reports;

public class DailyWaznaReport {

    private String profileName;
    private String serviceClass;
    private String points;
    private String transactionType;
    private String usedFor;
    private String addedByName;
    private String transactionDate;

    public DailyWaznaReport(String profileName, String serviceClass, String points, String transactionType, String usedFor, String addedByName, String transactionDate) {
        this.profileName = profileName;
        this.serviceClass = serviceClass;
        this.points = points;
        this.transactionType = transactionType;
        this.usedFor = usedFor;
        this.addedByName = addedByName;
        this.transactionDate = transactionDate;
    }

    public String getProfileName() {
        return profileName;
    }

    public void setProfileName(String profileName) {
        this.profileName = profileName;
    }

    public String getServiceClass() {
        return serviceClass;
    }

    public void setServiceClass(String serviceClass) {
        this.serviceClass = serviceClass;
    }

    public String getPoints() {
        return points;
    }

    public void setPoints(String points) {
        this.points = points;
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

    public String getAddedByName() {
        return addedByName;
    }

    public void setAddedByName(String addedByName) {
        this.addedByName = addedByName;
    }

    public String getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(String transactionDate) {
        this.transactionDate = transactionDate;
    }
}
