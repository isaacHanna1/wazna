package com.watad.services;


import com.watad.Common.YouthMeetingCalcPoints;
import com.watad.dto.PointsSummaryDTO;
import com.watad.entity.*;
import com.watad.exceptions.QrCodeException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public  class AttendanceProcessingServiceImp implements AttendanceProcessingService{

    private final AttendanceService attendanceService;
    private final QrCodeService qrCodeService;
    private final BonusTypeService bonusTypeService;
    private final SprintDataService sprintDataService;
    private final UserBounsService userBounsService;
    private final UserPointTransactionService userPointTransactionService;


    public AttendanceProcessingServiceImp(AttendanceService attendanceService, QrCodeService qrCodeService,BonusTypeService bonusTypeService,SprintDataService sprintDataService ,UserBounsService userBounsService , UserPointTransactionService userPointTransactionService) {
        this.attendanceService = attendanceService;
        this.qrCodeService = qrCodeService;
        this.bonusTypeService = bonusTypeService;
        this.sprintDataService = sprintDataService;
        this.userBounsService = userBounsService;
        this.userPointTransactionService = userPointTransactionService;
    }

    @Override
    public PointsSummaryDTO attendanceProcessing(User user , String code) {
            boolean isvalid = qrCodeService.isValid(code);
               if(isvalid){
                QrCode qrCode = qrCodeService.findByCode(code);
                if(qrCode.getMeetings().getId() != user.getProfile().getMeetings().getId()){
                    throw new QrCodeException("Sorry, this QR Code is not linked to your current meeting.");
                }
                Attendance attendance = new Attendance();
                handleAttendanceService(attendance,user,qrCode);
                double addPoint = handleUserBounsService(qrCode,attendance,user);
                handleUserPointTran(user , addPoint);
                Profile profile = user.getProfile();
                int curchId    = profile.getChurch().getId();
                int meetingID  = profile.getChurch().getId();
                double totalPoint = userPointTransactionService.getTotalPointsByProfileIdAndSprintId(user.getProfile().getId(),sprintDataService.getSprintDataByIsActive(curchId,meetingID).getId());
                return new PointsSummaryDTO(addPoint,totalPoint,user.getId(),profile.getFirstName(), profile.getLastName(), profile.getPhone());
            }
        return new PointsSummaryDTO(0.0 ,0.0);
    }

    private void handleAttendanceService(Attendance attendance , User user , QrCode qrCode){
        AttendanceId attendanceId = new AttendanceId();
        attendanceId.setUser(user);
        attendanceId.setQrCode(qrCode);
        attendance.setAttendanceId( attendanceId);
        attendanceService.save(attendance);
    }

    private double handleUserBounsService(QrCode qrCode,Attendance attendance ,User user){

        BonusType bonusType = bonusTypeService.getBonusTypeByDescription("Attendance");
        Profile profile     = user.getProfile();
        int curchId         = profile.getChurch().getId();
        int meetingID       = profile.getMeetings().getId();
        SprintData sprintData = sprintDataService.getSprintDataByIsActive(curchId,meetingID);
        if (bonusType == null || sprintData == null) {
            throw new IllegalArgumentException("User or QR code cannot be null or empty");
        }
            int points = bonusType.getPoint();
            double addedPoint = YouthMeetingCalcPoints.calculatePoints(qrCode.getValidStart(), qrCode.getValidEnd(), points, attendance.getScannedAt());
            UserBonus userBonus = new UserBonus(user.getProfile(), bonusType, addedPoint, sprintData, user, sprintData.getPointPrice(), bonusType.getPoint() , qrCode.getMeetings());
            userBounsService.save(userBonus);
            return  addedPoint;
        }


    private void handleUserPointTran(User user , double addPoint) {
        UserPointTransaction pointTransaction = new UserPointTransaction();
        pointTransaction.setProfile(user.getProfile());
        pointTransaction.setTransferTo(null);
        Profile profile = user.getProfile();
        int curchId    = profile.getChurch().getId();
        int meetingID  = profile.getMeetings().getId();
        pointTransaction.setSprintData(sprintDataService.getSprintDataByIsActive(curchId,meetingID));
        pointTransaction.setPoints(addPoint);
        pointTransaction.setActive(true);
        pointTransaction.setTransactionDate(LocalDateTime.now());
        pointTransaction.setUsedFor("Attendance Point");
        pointTransaction.setTransactionType("Earn");
        pointTransaction.setChurch(profile.getChurch());
        pointTransaction.setMeetings(profile.getMeetings());
        userPointTransactionService.save(pointTransaction);
    }


}

