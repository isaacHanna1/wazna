package com.watad.services;

import com.watad.Common.TimeUtil;
import com.watad.entity.*;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.time.LocalDateTime;

@Service
public class BonusAddingServiceImp implements BonusAddingService{

    private final ProfileService profileService;
    private final  UserServices userServices;
    private final  BonusTypeService bonusTypeService;
    private final UserBounsService userBounsService;
    private final SprintDataService sprintDataService ;
    private final UserPointTransactionService userPointTransactionService;
    private final TimeUtil timeUtil;

    public BonusAddingServiceImp(ProfileService profileService, UserServices userServices, BonusTypeService bonusTypeService, UserBounsService userBounsService, SprintDataService sprintDataService, UserPointTransactionService userPointTransactionService, TimeUtil timeUtil) {
        this.profileService = profileService;
        this.userServices = userServices;
        this.bonusTypeService = bonusTypeService;
        this.userBounsService = userBounsService;
        this.sprintDataService = sprintDataService;
        this.userPointTransactionService = userPointTransactionService;
        this.timeUtil = timeUtil;
    }

    @Override
    public void addNewBonus(int profileId, int userId, int bonusTypeId) {
        Profile theProfile    = profileService.getProfileById(profileId); // get profile
        User user             = userServices.findUserById(userId); // get user for same profile
        BonusType bonusType   = bonusTypeService.findById(bonusTypeId) ; // get the bounce type
        int churchId           = theProfile.getChurch().getId(); // get the church
        int meetingID         = theProfile.getMeetings().getId(); // get Meeting
        SprintData sprintData = sprintDataService.getSprintDataByIsActive(churchId,meetingID); // get sprint data
        UserBonus userBonus   = new UserBonus(theProfile, bonusType, bonusType.getPoint(), sprintData, user, sprintData.getPointPrice(), bonusType.getPoint() , theProfile.getMeetings());
        userBounsService.save(userBonus);
        handleUserPointTran(theProfile,bonusType.getPoint(),bonusType.getDescription(),userBonus);
    }

    private void handleUserPointTran(Profile profile , double addPoint , String bonceTypeDesc,UserBonus userBonus) {
        UserPointTransaction pointTransaction = new UserPointTransaction(); // make object of transaction
        pointTransaction.setProfile(profile);  //set the profile the take point
        pointTransaction.setTransferTo(null); // this not transfer
        int churchId    = profile.getChurch().getId();  // churchId logged in
        int meetingID  = profile.getMeetings().getId();  // logged in meeting
        pointTransaction.setSprintData(sprintDataService.getSprintDataByIsActive(churchId,meetingID)); // get active sprint
        pointTransaction.setPoints(addPoint); // set the gained points
        pointTransaction.setActive(true); // set as active
        pointTransaction.setTransactionDate(timeUtil.now()); // set the transaction time
        pointTransaction.setUsedFor(bonceTypeDesc); // description for points
        if(addPoint > 0) {
            pointTransaction.setTransactionType("Earn"); // type of point
        }else {
            pointTransaction.setTransactionType("Lose");// type of point
        }
        pointTransaction.setChurch(profile.getChurch()); // set Church
        pointTransaction.setMeetings(profile.getMeetings()); // set Meeting
        pointTransaction.setPointSource("MANUAL"); // add manual by admin leader server
        int loggedInProfileId =  userServices.getLogedInUserProfile().getId();
        pointTransaction.setAddedByProfileId(loggedInProfileId); // who added this
        pointTransaction.setUserBonus(userBonus);
        userPointTransactionService.save(pointTransaction);
    }
}
