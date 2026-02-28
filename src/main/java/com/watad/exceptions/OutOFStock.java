package com.watad.exceptions;

public class OutOFStock extends RuntimeException {
    public OutOFStock(String message) {
        super(message);
    }
}
