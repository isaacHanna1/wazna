package com.watad.services;

import com.watad.entity.*;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class BonusAddingServiceImp implements BonusAddingService{

    private final ProfileService profileService;
    private final  UserServices userServices;
    private final  BonusTypeService bonusTypeService;
    private final UserBounsService userBounsService;
    private final SprintDataService sprintDataService ;
    private final UserPointTransactionService userPointTransactionService;
    public BonusAddingServiceImp(ProfileService profileService, UserServices userServices, BonusTypeService bonusTypeService , UserBounsService userBounsService , SprintDataService sprintDataService , UserPointTransactionService userPointTransactionService) {
        this.profileService   = profileService;
        this.userServices     = userServices;
        this.bonusTypeService = bonusTypeService;
        this.userBounsService = userBounsService;
        this.sprintDataService= sprintDataService;
        this.userPointTransactionService = userPointTransactionService;
    }

    @Override
    public void addNewBonus(int profileId, int userId, int bonusTypeId) {
        Profile theProfile    = profileService.getProfileById(profileId);
        User user             = userServices.findUserById(userId);
        BonusType bonusType   = bonusTypeService.findById(bonusTypeId) ;
        int curchId           = theProfile.getChurch().getId();
        int meetingID         = theProfile.getMeetings().getId();
        SprintData sprintData = sprintDataService.getSprintDataByIsActive(curchId,meetingID);
        UserBonus userBonus   = new UserBonus(user.getProfile(), bonusType, bonusType.getPoint(), sprintData, user, sprintData.getPointPrice(), bonusType.getPoint() , theProfile.getMeetings());
        userBounsService.save(userBonus);
        handleUserPointTran(theProfile,bonusType.getPoint(),bonusType.getDescription());
    }

    private void handleUserPointTran(Profile profile , double addPoint , String bonceTypeDesc) {
        UserPointTransaction pointTransaction = new UserPointTransaction();
        pointTransaction.setProfile(profile);
        pointTransaction.setTransferTo(null);
        int curchId    = profile.getChurch().getId();
        int meetingID  = profile.getMeetings().getId();
        pointTransaction.setSprintData(sprintDataService.getSprintDataByIsActive(curchId,meetingID));
        pointTransaction.setPoints(addPoint);
        pointTransaction.setActive(true);
        pointTransaction.setTransactionDate(LocalDateTime.now());
        pointTransaction.setUsedFor(bonceTypeDesc);
        pointTransaction.setTransactionType("Earn");
        pointTransaction.setChurch(profile.getChurch());
        pointTransaction.setMeetings(profile.getMeetings());
        userPointTransactionService.save(pointTransaction);
    }
}
