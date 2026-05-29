package com.watad.services;

import com.watad.Common.TimeUtil;
import com.watad.entity.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class BonusAddingServiceImp implements BonusAddingService{

    private final ProfileService profileService;
    private final  UserServices userServices;
    private final  BonusTypeService bonusTypeService;
    private final UserBounsService userBounsService;
    private final SprintDataService sprintDataService ;
    private final UserPointTransactionService userPointTransactionService;
    private final TimeUtil timeUtil;



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

    private void handleUserPointTran(Profile profile, double addPoint, String bonceTypeDesc, UserBonus userBonus) {
        int churchId = profile.getChurch().getId();
        int meetingId = profile.getMeetings().getId();
        int loggedInProfileId = userServices.getLogedInUserProfile().getId();

        // Build the transaction object
        UserPointTransaction pointTransaction = UserPointTransaction.builder()
                .profile(profile)
                .transferTo(null)
                .sprintData(sprintDataService.getSprintDataByIsActive(churchId, meetingId))
                .points(addPoint)
                .isActive(true)
                .transactionDate(timeUtil.now())
                .usedFor(bonceTypeDesc)
                .transactionType(addPoint > 0 ? "Earn" : "Lose")
                .church(profile.getChurch())
                .meetings(profile.getMeetings())
                .pointSource("MANUAL")
                .addedByProfileId(loggedInProfileId)
                .userBonus(userBonus)
                .build();

        userPointTransactionService.save(pointTransaction);
    }
}
