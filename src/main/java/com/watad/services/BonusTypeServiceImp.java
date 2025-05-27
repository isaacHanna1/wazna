package com.watad.services;

import com.watad.dao.BonusTypeDao;
import com.watad.entity.BonusType;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class BonusTypeServiceImp implements  BonusTypeService{

    private final BonusTypeDao bonusTypeDao;

    public BonusTypeServiceImp(BonusTypeDao bonusTypeDao) {
        this.bonusTypeDao = bonusTypeDao;
    }

    @Override
    public BonusType getBonusTypeByDescription(String description) {
        return bonusTypeDao.getBonusTypeByDescription(description);
    }

    @Override
    public List<BonusType> findAll() {
        return bonusTypeDao.findAll();
    }

    public BonusType findById(int id){
        return bonusTypeDao.findById(id);
    }
}
