package com.watad.dto.cart;

import com.watad.enumValues.CartStatus;
import lombok.Data;

@Data
public class CartRequest {

    private int itemId;
    private int itemCount;
}
