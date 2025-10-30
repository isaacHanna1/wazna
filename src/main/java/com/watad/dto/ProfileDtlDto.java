package com.watad.dto;

import com.watad.entity.Church;
import com.watad.entity.Meetings;
import com.watad.entity.ServiceStage;
import com.watad.enumValues.Gender;

import java.time.LocalDate;
import java.util.List;

public class ProfileDtlDto {

    private int id ;
    private String firstName;
    private String lastName;
    private String gender;
    private List<Gender> genderList;
    private String serviceStage;
    private List<ServiceStage> serviceStageList;
    private String phone;
    private LocalDate birthday;
    private String address;
    private String fatherPeriest;
    private List<String> fatherPeriestList;
    private String ChurchName;
    private List<Church> churchList;
    private String MeetingName;
    private List<Meetings> meetingsList;
    private String userName;
    private double Points;
    private int rank;
    private String imagePath;
    private int age;
    private int churchId;
    private int meetingId;
    private int userId;
    private boolean isEnabled ; // staus of account in user table is enabled or not
    private String email;
    private String serviceClass;


    public ProfileDtlDto() {
    }

    public ProfileDtlDto(int id, String firstName, String lastName) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public ProfileDtlDto(int id, String firstName, String lastName,String phone) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
    }

    public ProfileDtlDto(int id, String firstName, String lastName , int meetingId , int churchId , int userId , String phone , String imagePath) {
        this.id = id;
        this.firstName = firstName;
        this.lastName  = lastName;
        this.churchId  = churchId;
        this.meetingId = meetingId;
        this.userId    = userId;
        this.phone     = phone;
        this.imagePath = imagePath;
    }

    public ProfileDtlDto(int id, String firstName, String lastName, String gender,
                         String serviceStage, String phone, LocalDate birthday,
                         String address, String fatherPeriest, String userName , String email) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.serviceStage = serviceStage;
        this.phone = phone;
        this.birthday = birthday;
        this.address = address;
        this.fatherPeriest = fatherPeriest;
        this.userName = userName;
        this.email = email;
    }

    public ProfileDtlDto(int id, String firstName, String lastName, Gender gender,
                         String serviceStage, String phone, LocalDate birthday,
                         String address, String fatherPeriest, String userName  , boolean isEnabled , int userId) {
        this.id = id;
        this.firstName  = firstName;
        this.lastName         = lastName;
        this.gender           = gender.toString();
        this.serviceStage     = serviceStage;
        this.phone = phone;
        this.birthday = birthday;
        this.address = address;
        this.fatherPeriest = fatherPeriest;
        this.userName = userName;
        this.isEnabled = isEnabled;
        this.userId   = userId;
    }




    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getServiceStage() {
        return serviceStage;
    }

    public void setServiceStage(String serviceStage) {
        this.serviceStage = serviceStage;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public LocalDate getBirthday() {
        return birthday;
    }

    public void setBirthday(LocalDate birthday) {
        this.birthday = birthday;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getFatherPeriest() {
        return fatherPeriest;
    }

    public void setFatherPeriest(String fatherPeriest) {
        this.fatherPeriest = fatherPeriest;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public List<Gender> getGenderList() {
        return genderList;
    }

    public void setGenderList(List<Gender> genderList) {
        this.genderList = genderList;
    }

    public List<ServiceStage> getServiceStageList() {
        return serviceStageList;
    }

    public void setServiceStageList(List<ServiceStage> serviceStageList) {
        this.serviceStageList = serviceStageList;
    }

    public List<String> getFatherPeriestList() {
        return fatherPeriestList;
    }

    public void setFatherPeriestList(List<String> fatherPeriestList) {
        this.fatherPeriestList = fatherPeriestList;
    }

    public String getChurchName() {
        return ChurchName;
    }

    public void setChurchName(String churchName) {
        ChurchName = churchName;
    }

    public List<Church> getChurchList() {
        return churchList;
    }

    public void setChurchList(List<Church> churchList) {
        this.churchList = churchList;
    }

    public String getMeetingName() {
        return MeetingName;
    }

    public void setMeetingName(String meetingName) {
        MeetingName = meetingName;
    }

    public List<Meetings> getMeetingsList() {
        return meetingsList;
    }

    public void setMeetingsList(List<Meetings> meetingsList) {
        this.meetingsList = meetingsList;
    }

    public double getPoints() {
        return Points;
    }

    public void setPoints(double points) {
        Points = points;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getChurchId() {
        return churchId;
    }

    public void setChurchId(int churchId) {
        this.churchId = churchId;
    }

    public int getMeetingId() {
        return meetingId;
    }

    public void setMeetingId(int meetingId) {
        this.meetingId = meetingId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;

    }

    public boolean isEnabled() {
        return isEnabled;
    }

    public void setEnabled(boolean enabled) {
        isEnabled = enabled;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getServiceClass() {
        return serviceClass;
    }

    public void setServiceClass(String serviceClass) {
        this.serviceClass = serviceClass;
    }
}
