package com.watad.entity;


import jakarta.persistence.*;

@Entity
@Table(name = "priests")
public class Priest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id ;

    @Column(name = "name")
    private String name ;

    @ManyToOne
    @JoinColumn(name = "dioceses_id")
    private Dioceses dioceses;


    public Priest() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Dioceses getDioceses() {
        return dioceses;
    }

    public void setDioceses(Dioceses dioceses) {
        this.dioceses = dioceses;
    }
}
