package com.watad.services;

import com.watad.dto.BonusTypeDto;
import com.watad.entity.BonusType;

import java.util.List;

public interface BonusTypeService {

    BonusType getBonusTypeByDescription(String description);
    public List<BonusTypeDto> findAll(String evaluationType);
    public List<BonusTypeDto> findAll();
    public BonusType findById(int id);
    void createBonusType(BonusType bonusType);
    List<BonusTypeDto> findByDesc(String desc ,String evaluationType);
    void updateBonusType(BonusType bonusType);
}
