package com.watad.dao;

import com.watad.entity.QrCode;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface QrCodeDao {

     Optional<QrCode> findByCode(String code);
     List<QrCode> getActiveByDate(LocalDate localDate , int churchId , int mettingId);
}
