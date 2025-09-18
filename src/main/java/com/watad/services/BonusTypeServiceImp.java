package com.watad.services;

import com.watad.dao.BonusTypeDao;
import com.watad.dto.BonusTypeDto;
import com.watad.entity.BonusType;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class BonusTypeServiceImp implements  BonusTypeService{

    private final BonusTypeDao bonusTypeDao;
    private final UserServices userServices;

    public BonusTypeServiceImp(BonusTypeDao bonusTypeDao, UserServices userServices) {
        this.bonusTypeDao = bonusTypeDao;
        this.userServices = userServices;
    }

    @Override
    public BonusType getBonusTypeByDescription(String description) {
        int churchId        = userServices.getLogInUserChurch().getId();
        int meetingId       = userServices.getLogInUserMeeting().getId();
        return bonusTypeDao.getBonusTypeByDescription(description,churchId,meetingId);
    }

    @Override
    public List<BonusTypeDto> findAll() {
        return bonusTypeDao.findAll();
    }

    public BonusType findById(int id){
        return bonusTypeDao.findById(id);
    }
}
