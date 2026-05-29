package com.watad.services;

import com.watad.dto.BonusTypeDto;
import com.watad.dto.bonusType.BonusTypeResponse;
import com.watad.entity.BonusType;

import java.util.List;

public interface BonusTypeService {

     List<BonusTypeDto> findAll(String evaluationType);
     List<BonusTypeResponse> findActiveBonus(String evaluationType);
     List<BonusTypeDto> findAll();
     List<BonusTypeResponse> findAll(String evaluationType,boolean active);
     BonusType findById(int id);
    void createBonusType(BonusType bonusType);
    List<BonusTypeDto> findByDesc(String desc ,String evaluationType);
    void updateBonusType(BonusType bonusType);
    List<BonusTypeDto> findAllActiveAndNotActive(String evaluationType);
    List<BonusTypeDto> findAllByAttendance(String evaluationType, String  physicalAttendanceRequired);

}
