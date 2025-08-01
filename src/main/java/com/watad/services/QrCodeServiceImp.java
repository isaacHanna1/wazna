package com.watad.services;

import com.watad.dao.QrCodeDao;
import com.watad.dto.QRCodeDto;
import com.watad.entity.Church;
import com.watad.entity.Meetings;
import com.watad.entity.QrCode;
import com.watad.exceptions.QrCodeException;
import jakarta.persistence.Query;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
public class QrCodeServiceImp implements  QrCodeService{

    private final QrCodeDao qrCodeDao;
    private final UserServices userServices;

    public QrCodeServiceImp(QrCodeDao qrCodeDao, UserServices userServices) {
        this.qrCodeDao = qrCodeDao;
        this.userServices = userServices;
    }

    @Override
    public QrCode findByCode(String code) {
        int churchId            = userServices.getLogInUserChurch().getId();
        int meetingID           = userServices.getLogInUserMeeting().getId();
        return qrCodeDao.findByCode(code,churchId,meetingID).orElseThrow(()->new QrCodeException("We Not Found QR Code"));
    }


    @Override
    public boolean isValid(String code) {
        QrCode      qrCode              = findByCode(code);
        LocalDateTime now               = LocalDateTime.now();
        LocalDate theTakenDate          = now.toLocalDate();
        LocalTime theTakenTime          = now.toLocalTime();
        boolean   isValidDate           = qrCode.getValidDate().equals(theTakenDate);
        boolean   isValidTime           = theTakenTime.isAfter(qrCode.getValidStart()) && theTakenTime.isBefore(qrCode.getValidEnd());

        if(!isValidDate)
            throw new QrCodeException("The Event Not Today Dear");
        if(!isValidTime)
            throw new QrCodeException("Not accept, Reason : Time must be between "+qrCode.getValidStart() +" , "+qrCode.getValidEnd());
        return true;
    }

    @Override
    public List<String> getActiveByDate(LocalDate localDate) {
        int churchId            = userServices.getLogInUserChurch().getId();
        int meetingID           = userServices.getLogInUserMeeting().getId();
        List<QrCode> codes      = qrCodeDao.getActiveByDate(localDate,churchId,meetingID);
        List<String> codeDesc   = new ArrayList<>();
        if(!codes.isEmpty()){
            for(QrCode code : codes){
                codeDesc.add(code.getCode());
            }
        }
        return codeDesc;
    }

    @Override
    @Transactional
    public QrCode create(QrCode qrCode) {

        Church church              = userServices.getLogInUserChurch();
        Meetings meeting           = userServices.getLogInUserMeeting();

        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
        qrCode.setValidStart(LocalTime.parse(qrCode.getValidStart().format(timeFormatter), timeFormatter));
        qrCode.setValidEnd(LocalTime.parse(qrCode.getValidEnd().format(timeFormatter), timeFormatter));
        qrCode.setMeetings(meeting);
        qrCode.setChurch(church);
        qrCode.setActive(true);
        return qrCodeDao.create(qrCode);
    }

    @Override
    public List<QrCode> getPaginatedQrCodes(LocalDate start , LocalDate end,int pageNumber, int pageSize) {
        int churchId            = userServices.getLogInUserChurch().getId();
        int meetingID           = userServices.getLogInUserMeeting().getId();
        return qrCodeDao.getPaginatedQrCodes(start , end , pageNumber,pageSize,churchId,meetingID);
    }

    @Override
    @Transactional
    public void update(QRCodeDto dto) {
        QrCode qrCode = findById(dto.getId());
        qrCode.setActive(dto.isActive());
        qrCodeDao.update(qrCode);
    }

    @Override
    public QrCode findById(int id) {
        int churchId            = userServices.getLogInUserChurch().getId();
        int meetingID           = userServices.getLogInUserMeeting().getId();
        return  qrCodeDao.findById(id,churchId,meetingID);
    }

    @Override
    public List<QrCode> findAll(LocalDate from , LocalDate to) {
        return  qrCodeDao.findAll(from , to);
    }


    @Override
    public QRCodeDto findQrCodeByCode(String code) {
        QrCode qrCode   =   findByCode(code);
        QRCodeDto qrCodeDto = new QRCodeDto();
        qrCodeDto.setDescription(qrCode.getDescription());
        qrCodeDto.setValiadFrom(qrCode.getValidDate().toString());
        qrCodeDto.setValiadTo(qrCode.getValidEnd().toString());
        return  qrCodeDto;
    }

}
