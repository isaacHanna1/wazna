package com.watad.entity;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "event_dtl")
public class EventDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 150, nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "is_paid")
    private Boolean isPaid;

    @Column(name="price")
    private int price;

    @Lob
    private byte[] image;

    @Column(length = 500)
    private String imageUrl;

    @Column(name = "max_spen_point")
    private Integer maxSpendPoint;

    @Column(name = "event_type_id")
    private Integer eventTypeId;
    @Column(name = "from_date")
    private LocalDate from_date;
    @Column(name = "to_date")
    private LocalDate to_date;
    // Constructors
    public EventDetail() {}


    // Getters and Setters

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getIsPaid() {
        return isPaid;
    }

    public void setIsPaid(Boolean isPaid) {
        this.isPaid = isPaid;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Integer getMaxSpendPoint() {
        return maxSpendPoint;
    }

    public void setMaxSpendPoint(Integer maxSpendPoint) {
        this.maxSpendPoint = maxSpendPoint;
    }

    public Integer getEventTypeId() {
        return eventTypeId;
    }

    public void setEventTypeId(Integer eventTypeId) {
        this.eventTypeId = eventTypeId;
    }

    public Boolean getPaid() {
        return isPaid;
    }

    public void setPaid(Boolean paid) {
        isPaid = paid;
    }

    public LocalDate getFrom_date() {
        return from_date;
    }

    public void setFrom_date(LocalDate from_date) {
        this.from_date = from_date;
    }

    public LocalDate getTo_date() {
        return to_date;
    }

    public void setTo_date(LocalDate to_date) {
        this.to_date = to_date;
    }
}
