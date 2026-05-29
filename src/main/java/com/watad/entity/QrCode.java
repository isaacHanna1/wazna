    package com.watad.entity;


    import jakarta.persistence.*;
    import lombok.Data;

    import java.time.LocalDate;
    import java.time.LocalDateTime;
    import java.time.LocalTime;

    @Entity
    @Table(name = "qr_codes")
    @Data
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



    }
