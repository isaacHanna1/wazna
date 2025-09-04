package com.watad.dao;

import com.watad.entity.BonusType;

import java.util.List;

public interface BonusTypeDao {

    BonusType getBonusTypeByDescription(String description, int churchId ,int meetingId  );
    List<BonusType> findAll();
    BonusType findById(int id);
}
