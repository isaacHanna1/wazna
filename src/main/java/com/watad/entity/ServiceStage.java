package com.watad.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "service_stage")
public class ServiceStage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;


    @Column(name="description")
    private String description;

    public ServiceStage() {
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
}
