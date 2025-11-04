package com.watad.services;

import com.watad.dao.BonusHeadDao;
import com.watad.entity.BonusHead;
import com.watad.entity.BonusType;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BonusHeadServiceImp implements  BonusHeadService{
    private final BonusHeadDao bonusHeadDao;

    public BonusHeadServiceImp(BonusHeadDao bonusHeadDao) {
        this.bonusHeadDao = bonusHeadDao;
    }

    @Override
    public List<BonusHead> findAllBonusService() {
        return bonusHeadDao.findAllBonusHead();
    }

    @Override
    public List<BonusHead> findByEvaluationType(String evaluationType) {
        return  bonusHeadDao.findBYEvaluationType(evaluationType);
    }
}
