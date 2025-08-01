package com.watad.dao;

import com.watad.entity.QrCode;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface QrCodeDao {

     Optional<QrCode> findByCode(String code ,int churchId ,int meetingId );
     List<QrCode> getActiveByDate(LocalDate localDate , int churchId , int mettingId);
     QrCode create(QrCode qrCode);
     List<QrCode> getPaginatedQrCodes(LocalDate start , LocalDate end ,int pageNumber, int pageSize, int churchId, int mettingId);
     void update(QrCode qrCode);
     QrCode findById(int id, int churchId,int meetingId);
     List<QrCode> findAll(LocalDate from , LocalDate to);
}
