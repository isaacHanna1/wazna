package com.watad.services;

import com.watad.dao.YouthRankDao;
import com.watad.dto.response.YouthRankDto;

import java.util.List;

public interface YouthRankService {

    List<YouthRankDto> getRankedYouth( int limit , int offset);
    List<YouthRankDto> getRankedYouthWithImage( int limit , int offset);
    List<YouthRankDto> getRankedYouthWithImageByUserName( int limit , String userName);
    public double getYouthPoint();
    public double getYouthPoint(int profileId, int churchId, int meetingId , int sprintId);
    int getSpecificYouthRank( );

}
