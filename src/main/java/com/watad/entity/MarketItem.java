package com.watad.entity;
import com.watad.entity.MarketCategory;
import jakarta.persistence.*;

@Entity
@Table(name = "market_item")
public class MarketItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "item_name")
    private String itemName;

    @Column(name = "item_desc")
    private String itemDesc;

    @Column(name = "points")
    private double points;

    @Column(name = "image_name")
    private String imageName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private MarketCategory category;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "church_id", nullable = false)
    private Church  church;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "meeting_id", nullable = false)
    private Meetings meeting;

    @Column(name = "status")
    private Boolean status;


    public MarketItem() {
    }

    public MarketItem(int id, String itemName, String itemDesc, double points, String imageName, MarketCategory category) {
        this.id = id;
        this.itemName = itemName;
        this.itemDesc = itemDesc;
        this.points = points;
        this.imageName = imageName;
        this.category = category;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemDesc() {
        return itemDesc;
    }

    public void setItemDesc(String itemDesc) {
        this.itemDesc = itemDesc;
    }

    public double getPoints() {
        return points;
    }

    public void setPoints(double points) {
        this.points = points;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public MarketCategory getCategory() {
        return category;
    }

    public void setCategory(MarketCategory category) {
        this.category = category;
    }

    public Meetings getMeeting() {
        return meeting;
    }

    public void setMeeting(Meetings meeting) {
        this.meeting = meeting;
    }

    public Church getChurch() {
        return church;
    }

    public void setChurch(Church church) {
        this.church = church;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "MarketItem{" +
                "id=" + id +
                ", itemName='" + itemName + '\'' +
                ", itemDesc='" + itemDesc + '\'' +
                ", points=" + points +
                ", imageName='" + imageName + '\'' +
                ", category=" + category +
                ", church=" + church +
                ", meeting=" + meeting +
                ", status=" + status +
                '}';
    }
}
