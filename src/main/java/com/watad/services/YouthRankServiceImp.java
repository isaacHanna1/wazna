package com.watad.services;

import com.watad.dao.YouthRankDao;
import com.watad.dto.response.YouthRankDto;
import com.watad.entity.Meetings;
import com.watad.entity.Profile;
import com.watad.entity.Role;
import com.watad.entity.User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class YouthRankServiceImp implements  YouthRankService{

    private final YouthRankDao youthRankDao;
    private final UserServices userServices ;
    private final SprintDataService sprintDataService;
    public YouthRankServiceImp(YouthRankDao youthRankDao , UserServices userServices , SprintDataService sprintDataService) {
        this.youthRankDao = youthRankDao;
        this.userServices = userServices;
        this.sprintDataService = sprintDataService;
    }


    @Override
    public List<YouthRankDto> getRankedYouth(int limit , int offset) {
        User user = userServices.logedInUser();
        int theMeetingId    = userServices.getLogInUserMeeting().getId();
        int theChurchId     = userServices.getLogInUserChurch().getId();
        int theSprintId     = userServices.getActiveSprint().getId();
        String user_roles = user.getRoles().stream()
                .map(role -> String.valueOf(role.getId()))
                .collect(Collectors.joining(","));

        return youthRankDao.getYouthRank(theSprintId,theChurchId,theMeetingId,user_roles,limit,offset);

    }

    @Override
    public List<YouthRankDto> getRankedYouthWithImage(int limit, int offset) {
        User user = userServices.logedInUser();
        int theMeetingId    = userServices.getLogInUserMeeting().getId();
        int theChurchId     = userServices.getLogInUserChurch().getId();
        int theSprintId     = userServices.getActiveSprint().getId();
        String user_roles  = "1";
        return youthRankDao.getYouthRankWithImage(theSprintId,theChurchId,theMeetingId,user_roles,limit,offset);
    }

    @Override
    public double getYouthPoint() {
        Profile theProfile  = userServices.getLogedInUserProfile();
        int theMeetingId    = userServices.getLogInUserMeeting().getId();
        int theChurchId     = userServices.getLogInUserChurch().getId();
        int theSprintId     = userServices.getActiveSprint().getId();
        return  youthRankDao.getYouthPoint(theProfile.getId(),theSprintId,theChurchId,theMeetingId);
    }

    @Override
    public int getSpecificYouthRank() {
        User user           = userServices.logedInUser();
        int theMeetingId    = userServices.getLogInUserMeeting().getId();
        int theChurchId     = userServices.getLogInUserChurch().getId();
        int theSprintId     = userServices.getActiveSprint().getId();
        String user_roles = user.getRoles().stream()
                .map(role -> String.valueOf(role.getId()))
                .collect(Collectors.joining(","));
        return youthRankDao.getSpecificYouthRank(theSprintId,theChurchId,theMeetingId,user.getProfile().getId(),user_roles);
    }
}
