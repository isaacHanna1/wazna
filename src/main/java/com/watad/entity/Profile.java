package com.watad.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.watad.enumValues.Gender;
import com.watad.validation.MinimumAge;
import com.watad.validation.UniquePhone;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name="profile")
public class Profile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="profile_id")
    private int id;

    @NotBlank(message = "First Name Is Mandatory")
    @Column(name ="first_name")
    @Pattern(
            regexp = "^[^\\s]+$",
            message = "First Name Must Not Contain Spaces"
    )
    private String firstName;

    @NotBlank(message = "Last Name Is Mandatory")
    @Column(name ="last_name")
    @Pattern(
            regexp = "^[^\\s]+$",
            message = "Last Name Must Not Contain Spaces"
    )
    private String lastName;

    @Enumerated(EnumType.STRING)
    @Column(name="gendre")
    @NotNull(message = "Choose Gender")
    private Gender gender;

    @OneToOne(cascade = {CascadeType.REFRESH, CascadeType.MERGE, CascadeType.DETACH})
    @JoinColumn(name="service_stage_id")
    @NotNull(message = "Choose Service Stage")
    private ServiceStage serviceStage;

    @Column(name="phone")
    @NotBlank(message = "Phone Number Is Mandatory")
    @Pattern(regexp = "^(\\+201|01)[0-2,5]{1}[0-9]{8}$", message = "Invalid Egyptian number")
  //  @UniquePhone(message = "Phone Number already exists")
    private String phone;
    
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Column(name = "birth_day")
    @NotNull(message = "Enter Your BirthDay")
    @MinimumAge(value = 17, message = "You must be at least 17 years old")
    private LocalDate birthday;

    @Column(name="address")
    @NotBlank(message = "Address  is mandatory")
    private String address;

    @Column(name="father_periest")
    @NotBlank(message = "Choose Father")
    private String fatherPeriest;

    @Column(name ="profile_image_path_server")
    private String profileImagePath;


    @Column(name="image_url")
    private String imageUrl;

    @Column(name = "join_date")
    @JsonFormat(pattern = "yyyy-MM-dd hh:mm a")
    private LocalDateTime joinDate;


    @OneToOne(mappedBy = "profile")
    private User user;

    @ManyToOne(cascade = {CascadeType.REFRESH, CascadeType.MERGE, CascadeType.DETACH})
    @JoinColumn(name = "meeting_id")
    @NotNull(message = "Choose Meeting")
    private Meetings meetings;
    @ManyToOne(cascade = {CascadeType.REFRESH, CascadeType.MERGE, CascadeType.DETACH})
    @JoinColumn(name = "church_id")
    @NotNull(message = "Choose Church")
    private Church church;


    public Profile() {
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

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public ServiceStage getServiceStage() {
        return serviceStage;
    }

    public void setServiceStage(ServiceStage serviceStage) {
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

    public String getProfileImagePath() {
        return profileImagePath;
    }

    public void setProfileImagePath(String profileImagePath) {
        this.profileImagePath = profileImagePath;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public LocalDateTime getJoinDate() {
        return joinDate;
    }

    public void setJoinDate(LocalDateTime joinDate) {
        this.joinDate = joinDate;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Meetings getMeetings() {
        return meetings;
    }

    public void setMeetings(Meetings meetings) {
        this.meetings = meetings;
    }

    public Church getChurch() {
        return church;
    }

    public void setChurch(Church church) {
        this.church = church;
    }
}
