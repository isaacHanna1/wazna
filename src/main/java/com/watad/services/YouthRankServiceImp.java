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
        Profile theProfile = user.getProfile();
        Meetings theMeeting = theProfile.getMeetings();
        int theMeetingId = theMeeting.getId();
        int theChurchId     = theProfile.getChurch().getId();
        int theSprintId     = sprintDataService.getSprintDataByIsActive(theChurchId,theMeetingId).getId();
        String user_roles = user.getRoles().stream()
                .map(role -> String.valueOf(role.getId()))
                .collect(Collectors.joining(","));

        List<YouthRankDto> listOfYouth =  youthRankDao.getYouthRank(theSprintId,theChurchId,theMeetingId,user_roles,limit,offset);
        if (listOfYouth.isEmpty()) {
            System.out.println("No youth found " );
        }
        return listOfYouth;
    }
    @Override
    public double getYouthPoint() {
        User user = userServices.logedInUser();
        Profile theProfile = user.getProfile();
        Meetings theMeeting = theProfile.getMeetings();
        int theMeetingId = theMeeting.getId();
        int theChurchId     = theProfile.getChurch().getId();
        int theSprintId     = sprintDataService.getSprintDataByIsActive(theChurchId,theMeetingId).getId();
        return  youthRankDao.getYouthPoint(theProfile.getId(),theSprintId,theChurchId,theMeetingId);
    }

    @Override
    public int getSpecificYouthRank() {
        User user = userServices.logedInUser();
        Profile theProfile = user.getProfile();
        Meetings theMeeting = theProfile.getMeetings();
        int theMeetingId = theMeeting.getId();
        int theChurchId     = theProfile.getChurch().getId();
        int theSprintId     = sprintDataService.getSprintDataByIsActive(theChurchId,theMeetingId).getId();
        String user_roles = user.getRoles().stream()
                .map(role -> String.valueOf(role.getId()))
                .collect(Collectors.joining(","));
        return youthRankDao.getSpecificYouthRank(theSprintId,theChurchId,theMeetingId,user.getProfile().getId(),user_roles);
    }
}
