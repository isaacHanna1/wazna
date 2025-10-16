package com.watad.dao;

import com.watad.dto.BonusTypeDto;
import com.watad.entity.BonusType;

import java.util.List;

public interface BonusTypeDao {

    BonusType getBonusTypeByDescription(String description, int churchId ,int meetingId  );
    List<BonusTypeDto> findAll(int churchId , int MeetingId);
    BonusType findById(int id);
    void createBonus(BonusType bonusType);
    List<BonusTypeDto> findByDesc(int churchId , int meetingId ,String desc);
    void updateBonusType(BonusType bonusType);
}
