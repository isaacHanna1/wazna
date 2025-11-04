package com.watad.services;

import com.watad.entity.BonusHead;
import com.watad.entity.BonusType;

import java.util.List;

public interface BonusHeadService {

    List<BonusHead> findAllBonusService();
    List<BonusHead> findByEvaluationType(String evaluationType);
}
