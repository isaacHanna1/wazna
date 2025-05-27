package com.watad.dao;
import com.watad.dto.ProfileDtlDto;
import com.watad.entity.UserPointTransaction;

import java.util.List;

public interface UserPointTransactionDao {
    void save(UserPointTransaction userPointTransaction);

    // Get total points for a specific user and sprint
    double getTotalPointsByProfileIdAndSprintId(int profileId, Integer sprintId);

    // find users by search by userName (phone)
    List<ProfileDtlDto> findProfileBuUserName(int chuch_id , int meeting_id , int sprint_id , String userPhone);
}
