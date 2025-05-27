package com.watad.services;

import com.watad.dao.QrCodeDao;
import com.watad.entity.QrCode;
import com.watad.exceptions.QrCodeException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Service
public class QrCodeServiceImp implements  QrCodeService{

    private final QrCodeDao qrCodeDao;

    public QrCodeServiceImp(QrCodeDao qrCodeDao) {
        this.qrCodeDao = qrCodeDao;
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


    public static void isValid() {

        QrCode qrCode = new QrCode();
        qrCode.setCode("YOUTH2025");
        qrCode.setValidDate(LocalDate.now());
        qrCode.setValidStart(LocalTime.of(19, 0)); // 7:00 PM
        qrCode.setValidEnd(LocalTime.of(22, 0));   // 10:00 PM
        qrCode.setDescription("Youth event QR code");

        LocalDateTime now  = LocalDateTime.now();
        LocalDate theDate  = now.toLocalDate();
        LocalTime theTime  = now.toLocalTime();

        if(qrCode.getValidDate().equals( theDate)){
            System.out.println("true");
        }else{
            System.out.println("false");
        }

        if(theTime.isAfter(qrCode.getValidStart()) && theTime.isBefore(qrCode.getValidEnd())){
            System.out.println("true");
        }else{
            System.out.println("false");
        }


    }

    public static void main(String []args){
        isValid();
    }

}
