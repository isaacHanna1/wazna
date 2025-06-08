package com.watad.services;

import com.watad.dao.UserPointTransactionDao;
import com.watad.dto.ProfileDtlDto;
import com.watad.entity.Meetings;
import com.watad.entity.Profile;
import com.watad.entity.User;
import com.watad.entity.UserPointTransaction;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserPointTransactionServiceImp implements UserPointTransactionService {
    private final UserServices userServices ;
    private final SprintDataService sprintDataService;
    private final UserPointTransactionDao userPointTransactionDao;

    public UserPointTransactionServiceImp(UserServices userServices, SprintDataService sprintDataService, UserPointTransactionDao userPointTransactionDao) {
        this.userServices = userServices;
        this.sprintDataService = sprintDataService;
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
}
