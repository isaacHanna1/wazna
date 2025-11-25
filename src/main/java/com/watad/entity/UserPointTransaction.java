package com.watad.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "user_point_transaction")
public class UserPointTransaction {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="transaction_id")
    private int id ;
    @ManyToOne()
    @JoinColumn(name = "profile_id")
    private Profile profile;
    @ManyToOne()
    @JoinColumn(name = "related_user_id")
    private Profile transferTo;
    @ManyToOne()
    @JoinColumn(name = "sprint_id")
    private SprintData sprintData;
    @Column(name = "points")
    private double points ;
    @Column(name="is_active")
    private boolean isActive;
    @Column(name = "transaction_date")
    private LocalDateTime transactionDate;
    @Column(name="transaction_type")
    private String transactionType;
    @Column(name="used_for")
    private String usedFor;

    @ManyToOne(cascade = {CascadeType.REFRESH, CascadeType.MERGE, CascadeType.DETACH})
    @JoinColumn(name = "church_id")
    private Church church;

    @ManyToOne(cascade = {CascadeType.REFRESH, CascadeType.MERGE, CascadeType.DETACH})
    @JoinColumn(name = "meeting_id")
    private Meetings meetings;

    @Column(name = "point_source_type")
    private String pointSource;

    @Column(name = "added_by_profile_id")
    private Integer addedByProfileId;


    public UserPointTransaction() {
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

    public Profile getTransferTo() {
        return transferTo;
    }

    public void setTransferTo(Profile transferTo) {
        this.transferTo = transferTo;
    }

    public SprintData getSprintData() {
        return sprintData;
    }

    public void setSprintData(SprintData sprintData) {
        this.sprintData = sprintData;
    }

    public double getPoints() {
        return points;
    }

    public void setPoints(double points) {
        this.points = points;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public LocalDateTime getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(LocalDateTime transactionDate) {
        this.transactionDate = transactionDate;
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

    public Church getChurch() {
        return church;
    }

    public void setChurch(Church church) {
        this.church = church;
    }

    public Meetings getMeetings() {
        return meetings;
    }

    public void setMeetings(Meetings meetings) {
        this.meetings = meetings;
    }

    public String getPointSource() {
        return pointSource;
    }

    public void setPointSource(String pointSource) {
        this.pointSource = pointSource;
    }

    public Integer getAddedByProfileId() {
        return addedByProfileId;
    }

    public void setAddedByProfileId(Integer addedByProfileId) {
        this.addedByProfileId = addedByProfileId;
    }
}
