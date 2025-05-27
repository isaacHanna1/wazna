package com.watad.services;

import com.watad.dao.YouthRankDao;
import com.watad.dto.response.YouthRankDto;

import java.util.List;

public interface YouthRankService {

    List<YouthRankDto> getRankedYouth( int limit , int offset);
    public double getYouthPoint();
    int getSpecificYouthRank( );

}
