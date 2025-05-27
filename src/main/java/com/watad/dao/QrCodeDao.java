package com.watad.dao;

import com.watad.entity.QrCode;

import java.util.Optional;

public interface QrCodeDao {

     Optional<QrCode> findByCode(String code);
}
