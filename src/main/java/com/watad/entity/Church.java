package com.watad.entity;


import jakarta.persistence.*;

@Entity
@Table(name = "church")
public class Church {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id ;
    @Column(name = "church_name")
    private String churchName;
    @ManyToOne(cascade = {CascadeType.REFRESH, CascadeType.MERGE, CascadeType.DETACH})
    @JoinColumn(name = "diocese_id")
    private Dioceses diocese;
    @Column(name = "town_or_village")
    private char townOrVillage;


    public Church() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getChurchName() {
        return churchName;
    }

    public void setChurchName(String churchName) {
        this.churchName = churchName;
    }

    public Dioceses getDiocese() {
        return diocese;
    }

    public void setDiocese(Dioceses diocese) {
        this.diocese = diocese;
    }

    public char getTownOrVillage() {
        return townOrVillage;
    }

    public void setTownOrVillage(char townOrVillage) {
        this.townOrVillage = townOrVillage;
    }
}
