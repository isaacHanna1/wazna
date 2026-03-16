package com.watad.services;

import com.watad.dao.QrCodeDao;
import com.watad.dto.QRCodeDto;
import com.watad.dto.bonusType.BonusTypeResponse;
import com.watad.dto.church.ChurchResponse;
import com.watad.dto.meeting.MeetingResponse;
import com.watad.dto.qrMeeting.QrMeetingDtoRequest;
import com.watad.dto.qrMeeting.QrMeetingDtoResponse;
import com.watad.entity.BonusType;
import com.watad.entity.Church;
import com.watad.entity.Meetings;
import com.watad.entity.QrCode;
import com.watad.exceptions.QrCodeException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
public class QrCodeServiceImp implements  QrCodeService{

    private final QrCodeDao qrCodeDao;
    private final UserServices userServices;
    private final BonusTypeService bonusTypeService;

    public QrCodeServiceImp(QrCodeDao qrCodeDao, UserServices userServices, BonusTypeService bonusTypeService) {
        this.qrCodeDao = qrCodeDao;
        this.userServices = userServices;
        this.bonusTypeService = bonusTypeService;
    }

    @Override
    public QrCode findByCode(String code) {
        int churchId            = userServices.getLogInUserChurch().getId();
        int meetingID           = userServices.getLogInUserMeeting().getId();
        return qrCodeDao.findByCode(code,churchId,meetingID).orElseThrow(()->new QrCodeException("We Not Found QR Code"));
    }


    @Override
    public boolean isValid(String code) {

        QrCode qrCode       = findByCode(code);
        // The server is hosted in Korea, but the application runs in Egypt.
        // We use the Egypt time zone to ensure correct date and time handling.
        ZoneId egyptZone    = ZoneId.of("Africa/Cairo");
        LocalDateTime now   = LocalDateTime.now(egyptZone);

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
        QrCode qrCode = findEntityById(dto.getId());
        System.out.println("the id is qr code  "+dto.getId());
        qrCode.setActive(dto.isActive());
        qrCodeDao.update(qrCode);
    }

    @Override
    @Transactional
    public void update(QrMeetingDtoRequest dto) {
        QrCode qrCode = dtoToModel(dto);
        qrCodeDao.update(qrCode);
    }

    @Override
    public QrMeetingDtoResponse findDtoById(int id) {
        int churchId            = userServices.getLogInUserChurch().getId();
        int meetingID           = userServices.getLogInUserMeeting().getId();
        QrCode  qrCode          =  qrCodeDao.findById(id,churchId,meetingID);
        return modelToDto(qrCode);
    }

    private QrMeetingDtoResponse modelToDto(QrCode qrCode) {

        QrMeetingDtoResponse qrMeetingDtoResponse = new QrMeetingDtoResponse();
        qrMeetingDtoResponse.setId(qrCode.getId());
        qrMeetingDtoResponse.setCode(qrCode.getCode());
        qrMeetingDtoResponse.setDescription(qrCode.getDescription());
        qrMeetingDtoResponse.setValidDate(qrCode.getValidDate());
        qrMeetingDtoResponse.setValidStart(qrCode.getValidStart());
        qrMeetingDtoResponse.setValidEnd(qrCode.getValidEnd());
        qrMeetingDtoResponse.setActive(qrCode.isActive());
        BonusTypeResponse bonusTypeResponse = new BonusTypeResponse();
        if (qrCode.getBonusType() != null) {
            bonusTypeResponse.setId(qrCode.getBonusType().getId());
            bonusTypeResponse.setDescription(qrCode.getBonusType().getDescription());
            qrMeetingDtoResponse.setBonusType(bonusTypeResponse);
        }
        qrMeetingDtoResponse.setBonusType(bonusTypeResponse);

        return  qrMeetingDtoResponse;
    }
    private QrCode dtoToModel(QrMeetingDtoRequest dto) {
        QrCode qrCode =  findEntityById(dto.getId());
        qrCode.setCode(dto.getCode());
        qrCode.setDescription(dto.getDescription());
        qrCode.setValidDate(dto.getValidDate());
        qrCode.setValidStart(dto.getValidStart());
        qrCode.setValidEnd(dto.getValidEnd());
        qrCode.setActive(dto.isActive());
        System.out.println("the bonus type id "+dto.getBonusTypeId());
        BonusType bonusType  = bonusTypeService.findById(dto.getBonusTypeId());
        Church church        = userServices.getLogInUserChurch();
        Meetings meeting     = userServices.getLogInUserMeeting();
        qrCode.setBonusType(bonusType);
        qrCode.setChurch(church);
        qrCode.setMeetings(meeting);
        return qrCode;
    }

        @Override
    public QrCode findEntityById(int id) {
        int churchId            = userServices.getLogInUserChurch().getId();
        int meetingID           = userServices.getLogInUserMeeting().getId();
        return  qrCodeDao.findById(id,churchId,meetingID);
    }

    @Override
    public List<QrCode> findAll(LocalDate from , LocalDate to) {
        int churchId            = userServices.getLogInUserChurch().getId();
        int meetingID           = userServices.getLogInUserMeeting().getId();
        return  qrCodeDao.findAll(from , to,churchId,meetingID);
    }

    @Override
    public List<QrMeetingDtoResponse> findInRangeAndBonusType(LocalDate fromDate, LocalDate toDate, int bonusTypeID) {
        int churchId            = userServices.getLogInUserChurch().getId();
        int meetingID           = userServices.getLogInUserMeeting().getId();
        return  qrCodeDao.findInRangeAndBonusType(churchId,meetingID,fromDate,toDate,bonusTypeID)
                .stream()
                .map(qrCode -> {
                    QrMeetingDtoResponse qrMeetingDtoResponse = new QrMeetingDtoResponse();
                    qrMeetingDtoResponse.setId(qrCode.getId());
                    qrMeetingDtoResponse.setCode(qrCode.getCode());
                    qrMeetingDtoResponse.setDescription(qrCode.getDescription());
                    return qrMeetingDtoResponse;
                }).toList();
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
