package com.watad.services;

import com.watad.dto.QRCodeDto;
import com.watad.entity.QrCode;

import java.time.LocalDate;
import java.util.List;

public interface QrCodeService {

    QrCode findByCode(String code);
    QRCodeDto findQrCodeByCode(String code);
    boolean isValid(String Code);
    public List<String> getActiveByDate(LocalDate localDate);
}
