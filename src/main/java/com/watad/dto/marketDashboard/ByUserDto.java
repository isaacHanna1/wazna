package com.watad.dto.marketDashboard;

import com.watad.enumValues.CartStatus;
import java.time.LocalDateTime;

public interface ByUserDto {
    Integer       getCartId();
    String        getFirstName();
    String        getLastName();
    String        getItemName();
    String        getSupplierName();
    Integer       getItemCount();
    Double        getPoints();
    LocalDateTime getPurchaseDate();
    CartStatus    getStatus();
}