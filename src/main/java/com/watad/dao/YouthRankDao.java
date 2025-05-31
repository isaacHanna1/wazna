package com.watad.dao;

import com.watad.dto.response.YouthRankDto;
import com.watad.entity.Profile;

import java.util.List;

public interface YouthRankDao {

    List<YouthRankDto> getYouthRank(int sprintId , int churchId , int meetingId ,String userRoles , int limit , int offset);
    double getYouthPoint(int profileId ,int sprintId , int churchId , int meetingId);
    int getSpecificYouthRank(int sprintId , int churchId , int meetingId , int profileId , String userRoles );
}
