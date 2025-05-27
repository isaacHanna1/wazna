package com.watad.dao;

import com.watad.entity.BonusType;

import java.util.List;

public interface BonusTypeDao {

    BonusType getBonusTypeByDescription(String description);
    List<BonusType> findAll();
    BonusType findById(int id);
}
