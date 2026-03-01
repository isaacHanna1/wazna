package com.watad.dto;

public class MarketItemDto {

    private int id ;
    private String itemName ;
    private String itemDesc;
    private double points ;
    private boolean status ;
    private int stockQuantity ;

    public MarketItemDto() {
    }
    public MarketItemDto(int id, boolean status) {
        this.id = id;
        this.status = status;
    }

    public MarketItemDto(int id, String itemName, String itemDesc, double points, boolean status) {
        this.id = id;
        this.itemName = itemName;
        this.itemDesc = itemDesc;
        this.points = points;
        this.status = status;
    }
    public MarketItemDto(int id, String itemName, String itemDesc, double points, boolean status , int stockQuantity) {
        this.id = id;
        this.itemName = itemName;
        this.itemDesc = itemDesc;
        this.points = points;
        this.status = status;
        this.stockQuantity = stockQuantity;
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

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "MarketItemDto{" +
                "id=" + id +
                ", itemName='" + itemName + '\'' +
                ", itemDesc='" + itemDesc + '\'' +
                ", points=" + points +
                ", status=" + status +
                '}';
    }

    public int getStockQuantity() {
        return stockQuantity;
    }

    public void setStockQuantity(int stockQuantity) {
        this.stockQuantity = stockQuantity;
    }
}
