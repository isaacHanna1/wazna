package com.watad.dto.cart;


import com.watad.enumValues.CartStatus;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class CartRespond {

    private int id;
    private int itemCount;
    private double points;


    private String status;
    private LocalDateTime purchaseDate;
    private ItemResponse item;

    @Data
    public static class ItemResponse {
        private String name;
        private String imageUrl;
    }
}