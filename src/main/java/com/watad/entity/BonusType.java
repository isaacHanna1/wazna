package com.watad.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "bonus_type")
@Data
public class BonusType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id ;

    @Column(name = "description")
    private String description;

    @Column(name = "points")
    private int point;

    @Column(name = "active_from")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate activeFrom;

    @Column(name = "active_to")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate activeTo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "meeting_id")
    private Meetings meetings;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "church_id")
    private Church church;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="bonus_head_id")
    private BonusHead bonusHead;


    @Column(name = "is_active")
    private boolean isActive;


    @Column(name = "is_physical_attendance_required")
    private boolean physicalAttendanceRequired;

}
