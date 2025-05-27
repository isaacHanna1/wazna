package com.watad.services;

import com.watad.entity.QrCode;

public interface QrCodeService {

    QrCode findByCode(String code);
    boolean isValid(String Code);
}
