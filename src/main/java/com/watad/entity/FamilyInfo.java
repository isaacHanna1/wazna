package com.watad.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "family_info")
public class FamilyInfo {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private int id;

        @OneToOne(mappedBy = "familyInfo")
        private Profile profile;


        // Father information
        @Column(name = "father_name", length = 100)
        private String fatherName;

        @Column(name = "father_telephone", length = 20)
        private String fatherTelephone;

        @Column(name = "father_exists")
        private Boolean fatherExists = true;


        // Mother information
        @Column(name = "mother_name", length = 100)
        private String motherName;

        @Column(name = "mother_telephone", length = 20)
        private String motherTelephone;

        @Column(name = "mother_exists")
        private Boolean motherExists = true;

        // Timestamps
        @Column(name = "created_at", updatable = false)
        private LocalDateTime createdAt = LocalDateTime.now();

        @Column(name = "updated_at")
        private LocalDateTime updatedAt = LocalDateTime.now();

        @PreUpdate
        public void preUpdate() {
            updatedAt = LocalDateTime.now();
        }

        // Getters and Setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Profile getProfile() { return profile; }
        public void setProfile(Profile profile) { this.profile = profile; }

        public String getFatherName() { return fatherName; }
        public void setFatherName(String fatherName) { this.fatherName = fatherName; }

        public String getFatherTelephone() { return fatherTelephone; }
        public void setFatherTelephone(String fatherTelephone) { this.fatherTelephone = fatherTelephone; }

        public Boolean getFatherExists() { return fatherExists; }
        public void setFatherExists(Boolean fatherExists) { this.fatherExists = fatherExists; }


        public String getMotherName() { return motherName; }
        public void setMotherName(String motherName) { this.motherName = motherName; }

        public String getMotherTelephone() { return motherTelephone; }
        public void setMotherTelephone(String motherTelephone) { this.motherTelephone = motherTelephone; }

        public Boolean getMotherExists() { return motherExists; }
        public void setMotherExists(Boolean motherExists) { this.motherExists = motherExists; }

        public LocalDateTime getCreatedAt() { return createdAt; }
        public LocalDateTime getUpdatedAt() { return updatedAt; }
}
