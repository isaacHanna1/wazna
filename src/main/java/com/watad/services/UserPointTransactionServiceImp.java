package com.watad.services;

import com.watad.dao.UserPointTransactionDao;
import com.watad.dto.PointTransactionSummaryDto;
import com.watad.dto.ProfileDtlDto;
import com.watad.entity.*;
import com.watad.exceptions.NotEnoughPointsException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserPointTransactionServiceImp implements UserPointTransactionService {
    private final UserServices userServices ;
    private final UserPointTransactionDao userPointTransactionDao;

    public UserPointTransactionServiceImp(UserServices userServices , UserPointTransactionDao userPointTransactionDao) {
        this.userServices = userServices;
        this.userPointTransactionDao = userPointTransactionDao;
    }

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

        SprintData sprint       = userServices.getActiveSprint();
        Church church           = userServices.getLogInUserChurch();
        Meetings meeting        = userServices.getLogInUserMeeting();
        Profile  profileFrom    = userServices.findUserById(fromUserId).getProfile();
        Profile  profileTo      = userServices.findUserById(toUserId).getProfile();
        double currentBalance   = getTotalPointsByProfileIdAndSprintId(profileFrom.getId(),sprint.getId());

        if(currentBalance>=point) {
            // 1. Add points to receiver
            UserPointTransaction toTransaction = new UserPointTransaction();
            toTransaction.setMeetings(meeting);
            toTransaction.setChurch(church);
            toTransaction.setSprintData(sprint);
            toTransaction.setPoints(point);
            toTransaction.setTransactionType("TRANSFER_IN");
            toTransaction.setTransactionDate(LocalDateTime.now());
            toTransaction.setProfile(profileTo); // we add the point to treasurer server
            toTransaction.setTransferTo(profileFrom); // who sent the points
            toTransaction.setActive(true);
            toTransaction.setUsedFor(reason + " من  : " + profileFrom.getFirstName() + " " + profileFrom.getLastName());
            toTransaction.setPointSource("MANUAL");
            toTransaction.setAddedByProfileId(profileFrom.getId());
            save(toTransaction);

            // 2. Deduct points from sender
            UserPointTransaction fromTransaction = new UserPointTransaction();
            fromTransaction.setMeetings(meeting);
            fromTransaction.setChurch(church);
            fromTransaction.setSprintData(sprint);
            fromTransaction.setPoints(point * -1);
            fromTransaction.setTransactionType("TRANSFER_OUT");
            fromTransaction.setTransactionDate(LocalDateTime.now());
            fromTransaction.setProfile(profileFrom);
            fromTransaction.setTransferTo(profileTo); // who received the points
            fromTransaction.setActive(true);
            fromTransaction.setUsedFor(" تحويل من حساب "+profileTo.getFirstName()+" "+profileTo.getLastName()+" " +" سبب التحويل  : "+ reason);
            fromTransaction.setPointSource("MANUAL");
            fromTransaction.setAddedByProfileId(profileFrom.getId());
            save(fromTransaction);
        }
        else{
            throw new NotEnoughPointsException("You don't have enough wazna points to complete this transfer.");
        }
        return true;
    }

    @Override
    public List<PointTransactionSummaryDto> getSummaryOfPoints(int profileId) {

        SprintData sprint       = userServices.getActiveSprint();
        Church church           = userServices.getLogInUserChurch();
        Meetings meeting        = userServices.getLogInUserMeeting();
        List<PointTransactionSummaryDto> summaryOfPoints = userPointTransactionDao.getSummaryOfPoints(profileId, sprint.getId(), church.getId(), meeting.getId());
        System.out.println("the size of summaryOfPoints is "+summaryOfPoints.size());
        return  summaryOfPoints;
    }
}
