package com.watad.exceptions;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
public enum ErrorCode {

    OUT_OF_STOCK ("STOCK_001", "Item is out of stock");

    private final String code;
    private final String defaultMessage;

    ErrorCode(String code, String defaultMessage) {
        this.code           = code;
        this.defaultMessage = defaultMessage;
    }

}
