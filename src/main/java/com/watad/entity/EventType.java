package com.watad.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "event_type")
public class EventType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "event_type_id")
    private Integer id;

    @Column(name = "event_name", length = 150, nullable = false)
    private String name;

    @Column(name = "active")
    private Boolean active;

    // Constructors
    public EventType() {}

    public EventType(String name, Boolean active) {
        this.name = name;
        this.active = active;
    }

    // Getters and Setters

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }
}
