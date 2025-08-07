package com.watad.entity;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name="sprint_data")
public class SprintData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "sprint_id")
    private int  id;
    @Column(name = "from_date")
    private LocalDate fromDate;
    @Column(name = "to_date")
    private LocalDate toDate;
    @Column(name = "is_active")
    private boolean isActive;
    @Column(name="point_price")
    private double pointPrice;
    @ManyToOne(cascade = {CascadeType.REFRESH, CascadeType.MERGE, CascadeType.DETACH})
    @JoinColumn(name = "church_id")
    private Church church;
    @ManyToOne(cascade = {CascadeType.REFRESH, CascadeType.MERGE, CascadeType.DETACH})
    @JoinColumn(name = "meeting_id")
    private Meetings meetings;

    public SprintData() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDate getFromDate() {
        return fromDate;
    }

    public void setFromDate(LocalDate fromDate) {
        this.fromDate = fromDate;
    }

    public LocalDate getToDate() {
        return toDate;
    }

    public void setToDate(LocalDate toDate) {
        this.toDate = toDate;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public double getPointPrice() {
        return pointPrice;
    }

    public void setPointPrice(double pointPrice) {
        this.pointPrice = pointPrice;
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
}
