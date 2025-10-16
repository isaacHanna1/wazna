    package com.watad.entity;


    import jakarta.persistence.*;

    import java.time.LocalDate;
    import java.time.LocalDateTime;
    import java.time.LocalTime;

    @Entity
    @Table(name = "qr_codes")
    public class QrCode {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY )
        private int id ;
        @Column(name = "code")
        private String code;

        @Column(name = "valid_date")
        private LocalDate validDate;

        @Column(name = "valid_start")
        private LocalTime validStart;

        @Column(name = "valid_end")
        private LocalTime validEnd;

        @Column(name = "description")
        private String description;

        @Column(name = "created_at")
        private LocalDateTime createAt = LocalDateTime.now();

        @ManyToOne(cascade = {CascadeType.REFRESH, CascadeType.MERGE, CascadeType.DETACH})
        @JoinColumn( name = "meeting_id")
        private Meetings meetings ;

        @ManyToOne(cascade = {CascadeType.REFRESH, CascadeType.MERGE, CascadeType.DETACH})
        @JoinColumn( name = "church_id")
        private Church church ;

        @Column(name = "is_active")
        private boolean active;


        @ManyToOne(cascade = {CascadeType.REFRESH, CascadeType.MERGE, CascadeType.DETACH})
        @JoinColumn(name = "bonus_type_id")
        private BonusType bonusType ;


        public QrCode() {
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public LocalDate getValidDate() {
            return validDate;
        }

        public void setValidDate(LocalDate validDate) {
            this.validDate = validDate;
        }

        public LocalTime getValidStart() {
            return validStart;
        }

        public void setValidStart(LocalTime validStart) {
            this.validStart = validStart;
        }

        public LocalTime getValidEnd() {
            return validEnd;
        }

        public void setValidEnd(LocalTime validEnd) {
            this.validEnd = validEnd;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public LocalDateTime getCreateAt() {
            return createAt;
        }

        public void setCreateAt(LocalDateTime createAt) {
            this.createAt = createAt;
        }

        public Meetings getMeetings() {
            return meetings;
        }

        public void setMeetings(Meetings meetings) {
            this.meetings = meetings;
        }

        public Church getChurch() {
            return church;
        }

        public void setChurch(Church church) {
            this.church = church;
        }

        public boolean isActive() {
            return active;
        }

        public void setActive(boolean active) {
            this.active = active;
        }


        public BonusType getBonusType() {
            return bonusType;
        }

        public void setBonusType(BonusType bonusType) {
            this.bonusType = bonusType;
        }
    }
