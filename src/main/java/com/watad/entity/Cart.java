package com.watad.entity;

import com.watad.enumValues.CartStatus;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "cart")
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id ;

    @ManyToOne()
    @JoinColumn(name = "item_id")
    private MarketItem marketItem;


    @Column(name = "item_count")
    private int itemCount; // number of items

    @Column(name = "wazna_points")
    private double waznaPoints; // the price of item

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private CartStatus status = CartStatus.OPEN; // the cart status so we can closed this item cannot be edit

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sprint_id")
    private SprintData sprint;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "church_id")
    private Church church;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "meeting_id")
    private Meetings meeting;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @CreationTimestamp
    @Column(name = "purchase_date")
    private LocalDateTime PurchaseDate;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "update_at")
    private LocalDateTime updateAt;

}
