package com.watad.services;

import com.watad.dao.QrCodeDao;
import com.watad.dto.QRCodeDto;
import com.watad.entity.Meetings;
import com.watad.entity.QrCode;
import com.watad.exceptions.QrCodeException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
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
        return qrCodeDao.findByCode(code).orElseThrow(()->new QrCodeException("We Not Found QR Code"));
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
    public QRCodeDto findQrCodeByCode(String code) {
        QrCode qrCode   =   findByCode(code);
        QRCodeDto qrCodeDto = new QRCodeDto();
        qrCodeDto.setDescription(qrCode.getDescription());
        qrCodeDto.setValiadFrom(qrCode.getValidDate().toString());
        qrCodeDto.setValiadTo(qrCode.getValidEnd().toString());
        return  qrCodeDto;
    }

}
