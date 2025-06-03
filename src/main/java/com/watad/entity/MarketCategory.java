package com.watad.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "market_category")
public class MarketCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private int id ;
    @Column(name = "description")
    private String description;
    @ManyToOne(cascade = {CascadeType.REFRESH,CascadeType.DETACH} ,fetch = FetchType.LAZY)
    @JoinColumn(name = "church_id")
    private Church church;
    @ManyToOne(cascade = {CascadeType.REFRESH,CascadeType.DETACH} ,fetch = FetchType.LAZY)
    @JoinColumn(name = "meeting_id")
    private Meetings meeting;
    @Column(name = "active")
    private boolean isActive ;

    public MarketCategory() {
    }

    public MarketCategory(int id, String description) {
        this.id = id;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Church getChurch() {
        return church;
    }

    public void setChurch(Church church) {
        this.church = church;
    }

    public Meetings getMeeting() {
        return meeting;
    }

    public void setMeeting(Meetings meeting) {
        this.meeting = meeting;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }
}
