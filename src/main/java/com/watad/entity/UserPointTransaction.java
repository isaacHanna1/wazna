package com.watad.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "user_point_transaction")
@Data
@Builder
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

    @ManyToOne(cascade = {CascadeType.REFRESH, CascadeType.MERGE, CascadeType.DETACH})
    @JoinColumn(name = "bonus_id")
    private UserBonus userBonus;


}
