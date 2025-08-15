package com.watad.services;

import com.watad.dto.PointTransactionSummaryDto;
import com.watad.dto.ProfileDtlDto;
import com.watad.entity.UserPointTransaction;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

public interface UserPointTransactionService {


    void save(UserPointTransaction userPointTransaction);
    public Double getTotalPointsByProfileIdAndSprintId(int profileId, Integer sprintId);
    public List<ProfileDtlDto> findProfileByUserName( String userPhone);
    boolean transferPoint( int fromUserId , int toUserId , double point , String reason);
    List<PointTransactionSummaryDto> getSummaryOfPoints();

    }
