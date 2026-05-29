package com.watad.services;


import com.watad.Common.YouthMeetingCalcPoints;
import com.watad.dto.PointsSummaryDTO;
import com.watad.entity.*;
import com.watad.exceptions.QrCodeException;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;


@Service
@RequiredArgsConstructor
public  class AttendanceProcessingServiceImp implements AttendanceProcessingService{

    private static final Logger log = LoggerFactory.getLogger(AttendanceProcessingServiceImp.class);
    private final AttendanceService attendanceService;
    private final QrCodeService qrCodeService;
    private final BonusTypeService bonusTypeService;
    private final SprintDataService sprintDataService;
    private final UserBounsService userBounsService;
    private final UserPointTransactionService userPointTransactionService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public PointsSummaryDTO attendanceProcessing(User user, String code, LocalDate theTakenDate, LocalTime scannedAt) {

        // Validation is based on Egypt local time.
        // The ZoneId is applied in the isValid() method,
        // and the Attendance class also checks the scannedAt time accordingly.

        System.out.println("Im called ");
        boolean isValid = qrCodeService.isValid(code,theTakenDate,scannedAt);
               if(isValid){
                QrCode qrCode = qrCodeService.findByCode(code);
                if(qrCode.getMeetings().getId() != user.getProfile().getMeetings().getId()){
                    throw new QrCodeException("Sorry, this QR Code is not linked to your current meeting.");
                }
                Attendance attendance = new Attendance();
                // to handle manual attendance
                attendance.setScannedAt(scannedAt);
                handleAttendanceService(attendance,user,qrCode);
                UserBonus userBonus = handleUserBonusService(qrCode,attendance,user);
                double addPoint     = userBonus.getBouncePoint();
                handleUserPointTran(user , addPoint ,qrCode.getBonusType().getDescription(),userBonus);
                Profile profile         = user.getProfile();
                int churchId             = profile.getChurch().getId();
                int meetingID           = profile.getMeetings().getId();
                double totalPoint       = userPointTransactionService.getTotalPointsByProfileIdAndSprintId(user.getProfile().getId(),sprintDataService.getSprintDataByIsActive(churchId,meetingID).getId());
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

    private UserBonus handleUserBonusService(QrCode qrCode,Attendance attendance ,User user){

        int bonusTypeId = qrCode.getBonusType().getId();
        BonusType bonusType = bonusTypeService.findById(bonusTypeId);
        Profile profile     = user.getProfile();
        int churchId         = profile.getChurch().getId();
        int meetingID       = profile.getMeetings().getId();
        SprintData sprintData = sprintDataService.getSprintDataByIsActive(churchId,meetingID);
        if (bonusType == null || sprintData == null) {
            System.out.println("User or QR code cannot be null or empty");
            throw new IllegalArgumentException("Internal Error Happened !");

        }
        log.info("the scanned at is {} ",attendance.getScannedAt());
            int points = bonusType.getPoint();
            double addedPoint = YouthMeetingCalcPoints.calculatePoints(qrCode.getValidStart(), qrCode.getValidEnd(), points, attendance.getScannedAt());
            UserBonus userBonus = new UserBonus(user.getProfile(), bonusType, addedPoint, sprintData, user, sprintData.getPointPrice(), bonusType.getPoint() , qrCode.getMeetings());
            userBounsService.save(userBonus);
            return  userBonus;
        }


    private void handleUserPointTran(User user, double addPoint, String usedFor, UserBonus userBonus) {
        Profile profile = user.getProfile();

        // Fetch dependencies
        var sprintData = sprintDataService.getSprintDataByIsActive(
                profile.getChurch().getId(),
                profile.getMeetings().getId()
        );

        // Build the object
        UserPointTransaction pointTransaction = UserPointTransaction.builder()
                .profile(profile)
                .transferTo(null)
                .sprintData(sprintData)
                .points(addPoint)
                .isActive(true)
                .transactionDate(LocalDateTime.now())
                .usedFor(usedFor)
                .transactionType("Earn")
                .church(profile.getChurch())
                .pointSource("SYSTEM")
                .addedByProfileId(null)
                .meetings(profile.getMeetings())
                .userBonus(userBonus)
                .build();

        userPointTransactionService.save(pointTransaction);
    }


}

