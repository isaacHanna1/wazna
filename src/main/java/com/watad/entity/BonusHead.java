package com.watad.entity;

import com.watad.Common.TimeUtil;
import jakarta.persistence.*;
import lombok.Data;
import org.springframework.cglib.core.Local;

import java.time.LocalDate;

@Entity
@Table(name = "bonus_head")
@Data
public class BonusHead {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id ;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "active_yn")
    private boolean active;

    @Column(name = "created_at")
    private LocalDate createdAt;

    @Column(name = "evaluation_type")
    private String evaluationType;


}
