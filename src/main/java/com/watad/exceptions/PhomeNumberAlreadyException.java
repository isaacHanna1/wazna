package com.watad.exceptions;

public class PhomeNumberAlreadyException extends RuntimeException{

    
    public PhomeNumberAlreadyException(String message) {
        super("This Phone Number Already Exists");
    }
}
