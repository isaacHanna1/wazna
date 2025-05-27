package com.watad.services;

import com.watad.entity.BonusType;

import java.util.List;

public interface BonusTypeService {

    BonusType getBonusTypeByDescription(String description);
    public List<BonusType> findAll();
    public BonusType findById(int id);
}
