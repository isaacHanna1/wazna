package com.watad.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "user_bonus")
public class UserBonus {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id ;
    @ManyToOne()
    @JoinColumn(name="profile_id")
    private Profile profile;

    @ManyToOne(cascade = {CascadeType.REFRESH, CascadeType.MERGE, CascadeType.DETACH})
    @JoinColumn(name = "bonus_type_id")
    private BonusType bonusType;

    @Column(name = "bounce_point")
    private double BouncePoint;

    @ManyToOne()
    @JoinColumn(name = "sprint_id")
    private SprintData sprintData;

    @Column(name = "added_date")
    private LocalDateTime addedDate;


    @ManyToOne(cascade = {CascadeType.REFRESH, CascadeType.MERGE, CascadeType.DETACH})
    @JoinColumn(name = "addBy_user_id")
    private User addByUserId;


    @Column(name ="point_price")
    private double pointPrice;
    @Column(name ="bonce_type_point")
    private int bonceTypePoint ;

    @ManyToOne(cascade = {CascadeType.REFRESH, CascadeType.MERGE, CascadeType.DETACH})
    @JoinColumn(name = "meeting_id")
    private Meetings meetings;
    public UserBonus() {
    }

    public UserBonus(Profile profile, BonusType bonusType, double bouncePoint, SprintData sprintData, User addByUserId, double pointPrice, int bonceTypePoint , Meetings meetings) {
        this.profile = profile;
        this.bonusType = bonusType;
        this.BouncePoint = bouncePoint;
        this.sprintData = sprintData;
        this.addedDate = LocalDateTime.now();
        this.addByUserId = addByUserId;
        this.pointPrice = pointPrice;
        this.bonceTypePoint = bonceTypePoint;
        this.meetings = meetings;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Profile getProfile() {
        return profile;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }

    public BonusType getBonusType() {
        return bonusType;
    }

    public void setBonusType(BonusType bonusType) {
        this.bonusType = bonusType;
    }

    public double getBouncePoint() {
        return BouncePoint;
    }

    public void setBouncePoint(double bouncePoint) {
        BouncePoint = bouncePoint;
    }

    public SprintData getSprintData() {
        return sprintData;
    }

    public void setSprintData(SprintData sprintData) {
        this.sprintData = sprintData;
    }

    public LocalDateTime getAddedDate() {
        return addedDate;
    }

    public void setAddedDate(LocalDateTime addedDate) {
        this.addedDate = addedDate;
    }

    public User getAddByUserId() {
        return addByUserId;
    }

    public void setAddByUserId(User addByUserId) {
        this.addByUserId = addByUserId;
    }

    public double getPointPrice() {
        return pointPrice;
    }

    public void setPointPrice(double pointPrice) {
        this.pointPrice = pointPrice;
    }

    public int getBonceTypePoint() {
        return bonceTypePoint;
    }

    public void setBonceTypePoint(int bonceTypePoint) {
        this.bonceTypePoint = bonceTypePoint;
    }
}
