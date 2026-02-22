package com.watad.services;


import com.watad.Common.YouthMeetingCalcPoints;
import com.watad.dto.PointsSummaryDTO;
import com.watad.entity.*;
import com.watad.exceptions.QrCodeException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
    @Transactional(rollbackFor = Exception.class)
    public PointsSummaryDTO attendanceProcessing(User user , String code) {
        // Validation is based on Egypt local time.
        // The ZoneId is applied in the isValid() method,
        // and the Attendance class also checks the scannedAt time accordingly.
        boolean isvalid = qrCodeService.isValid(code);
               if(isvalid){
                QrCode qrCode = qrCodeService.findByCode(code);
                if(qrCode.getMeetings().getId() != user.getProfile().getMeetings().getId()){
                    throw new QrCodeException("Sorry, this QR Code is not linked to your current meeting.");
                }
                Attendance attendance = new Attendance();
                handleAttendanceService(attendance,user,qrCode);
                UserBonus userBonus = handleUserBounsService(qrCode,attendance,user);
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

    private UserBonus handleUserBounsService(QrCode qrCode,Attendance attendance ,User user){

        int bonusTypeId = qrCode.getBonusType().getId();
        BonusType bonusType = bonusTypeService.findById(bonusTypeId);
        Profile profile     = user.getProfile();
        int curchId         = profile.getChurch().getId();
        int meetingID       = profile.getMeetings().getId();
        SprintData sprintData = sprintDataService.getSprintDataByIsActive(curchId,meetingID);
        if (bonusType == null || sprintData == null) {
            System.out.println("User or QR code cannot be null or empty");
            throw new IllegalArgumentException("Internal Error Happened !");

        }
            int points = bonusType.getPoint();
            double addedPoint = YouthMeetingCalcPoints.calculatePoints(qrCode.getValidStart(), qrCode.getValidEnd(), points, attendance.getScannedAt());
            UserBonus userBonus = new UserBonus(user.getProfile(), bonusType, addedPoint, sprintData, user, sprintData.getPointPrice(), bonusType.getPoint() , qrCode.getMeetings());
            userBounsService.save(userBonus);
            return  userBonus;
        }


    private void handleUserPointTran(User user , double addPoint,String  usedFor,UserBonus userBonus) {
        UserPointTransaction pointTransaction = new UserPointTransaction();
        pointTransaction.setProfile(user.getProfile());
        pointTransaction.setTransferTo(null);
        Profile profile = user.getProfile();
        int churchId    = profile.getChurch().getId();
        int meetingID   = profile.getMeetings().getId();
        pointTransaction.setSprintData(sprintDataService.getSprintDataByIsActive(churchId,meetingID));
        pointTransaction.setPoints(addPoint);
        pointTransaction.setActive(true);
        pointTransaction.setTransactionDate(LocalDateTime.now());
        pointTransaction.setUsedFor(usedFor);
        pointTransaction.setTransactionType("Earn");
        pointTransaction.setChurch(profile.getChurch());
        pointTransaction.setPointSource("SYSTEM");
        pointTransaction.setAddedByProfileId(null); // even if the admin insert manually but the admin can not control the num of point  it based on the time
        pointTransaction.setMeetings(profile.getMeetings());
        pointTransaction.setUserBonus(userBonus);
        userPointTransactionService.save(pointTransaction);
    }


}

