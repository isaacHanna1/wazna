package com.watad.services;

import com.watad.dao.UserPointTransactionDao;
import com.watad.dto.PointTransactionSummaryDto;
import com.watad.dto.ProfileDtlDto;
import com.watad.entity.*;
import com.watad.exceptions.NotEnoughPointsException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserPointTransactionServiceImp implements UserPointTransactionService {
    private final UserServices userServices ;
    private final UserPointTransactionDao userPointTransactionDao;

    @Override
    public void save(UserPointTransaction userPointTransaction) {
        userPointTransactionDao.save(userPointTransaction);
    }

    @Override
    public Double getTotalPointsByProfileIdAndSprintId(int profileId, Integer sprintId) {
        return userPointTransactionDao.getTotalPointsByProfileIdAndSprintId (profileId,sprintId);
    }

    @Override
    public List<ProfileDtlDto> findProfileByUserName(String userPhone) {
        User user           = userServices.logedInUser();
        int theMeetingId    = userServices.getLogInUserMeeting().getId();
        int theChurchId     = userServices.getLogInUserChurch().getId();
        int theSprintId     = userServices.getActiveSprint().getId();
        int roleId = user.getRoles().iterator().next().getId()-1; // for single role we -1 to find the user have lowe role in system to give him point
        return userPointTransactionDao.findProfileBuUserName(theChurchId,theMeetingId,theSprintId,userPhone,roleId);
    }

    @Override
    public boolean transferPoint(int fromUserId, int toUserId, double point, String reason) {
        SprintData sprint           = userServices.getActiveSprint();
        Church church               = userServices.getLogInUserChurch();
        Meetings meeting            = userServices.getLogInUserMeeting();
        Profile profileFrom         = userServices.findUserById(fromUserId).getProfile();
        Profile profileTo           = userServices.findUserById(toUserId).getProfile();
        double currentBalance       = getTotalPointsByProfileIdAndSprintId(profileFrom.getId(), sprint.getId());

        if (currentBalance < point) {
            throw new NotEnoughPointsException("You don't have enough wazna points to complete this transfer.");
        }

        // Common metadata for both transactions
        var baseBuilder = UserPointTransaction.builder()
                .meetings(meeting)
                .church(church)
                .sprintData(sprint)
                .transactionDate(LocalDateTime.now())
                .isActive(true)
                .pointSource("MANUAL")
                .addedByProfileId(profileFrom.getId());

        // 1. Add points to receiver (TRANSFER_IN)
        save(baseBuilder
                .profile(profileTo)
                .transferTo(profileFrom)
                .points(point)
                .transactionType("TRANSFER_IN")
                .usedFor(reason + " من : " + profileFrom.getFirstName() + " " + profileFrom.getLastName())
                .build());

        // 2. Deduct points from sender (TRANSFER_OUT)
        save(baseBuilder
                .profile(profileFrom)
                .transferTo(profileTo)
                .points(point * -1)
                .transactionType("TRANSFER_OUT")
                .usedFor(" تحويل إلى حساب " + profileTo.getFirstName() + " " + profileTo.getLastName() + " سبب التحويل : " + reason)
                .build());

        return true;
    }

    @Override
    public List<PointTransactionSummaryDto> getSummaryOfPoints(int profileId) {

        SprintData sprint       = userServices.getActiveSprint();
        Church church           = userServices.getLogInUserChurch();
        Meetings meeting        = userServices.getLogInUserMeeting();
        List<PointTransactionSummaryDto> summaryOfPoints = userPointTransactionDao.getSummaryOfPoints(profileId, sprint.getId(), church.getId(), meeting.getId());
        return  summaryOfPoints;
    }
}
