package com.watad.services;

import com.watad.dto.ProfileDtlDto;
import com.watad.entity.UserPointTransaction;

import java.util.List;

public interface UserPointTransactionService {


    void save(UserPointTransaction userPointTransaction);
    public Double getTotalPointsByProfileIdAndSprintId(int profileId, Integer sprintId);
    public List<ProfileDtlDto> findProfileByUserName( String userPhone);

    }
