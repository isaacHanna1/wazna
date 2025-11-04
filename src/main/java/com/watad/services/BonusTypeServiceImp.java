package com.watad.services;

import com.watad.dao.BonusTypeDao;
import com.watad.dto.BonusTypeDto;
import com.watad.entity.BonusType;
import com.watad.entity.Church;
import com.watad.entity.Meetings;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

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
        int churchId        = userServices.getLogInUserChurch().getId();
        int meetingId       = userServices.getLogInUserMeeting().getId();
        return bonusTypeDao.findAll(churchId,meetingId);
    }
    @Override
    public List<BonusTypeDto> findAll(String evaluationType) {
        int churchId        = userServices.getLogInUserChurch().getId();
        int meetingId       = userServices.getLogInUserMeeting().getId();
        return bonusTypeDao.findAll(churchId,meetingId,evaluationType);
    }


    public BonusType findById(int id){
        return bonusTypeDao.findById(id);
    }

    @Override
    @Transactional
    public void createBonusType(BonusType bonusType) {
        Church church           = userServices.getLogInUserChurch();
        Meetings meetings       = userServices.getLogInUserMeeting();
        bonusType.setChurch(church);
        bonusType.setMeetings(meetings);
        bonusTypeDao.createBonus(bonusType);
    }

    @Override
    public List<BonusTypeDto> findByDesc(String desc , String evaluationType) {
        Church church           = userServices.getLogInUserChurch();
        Meetings meetings       = userServices.getLogInUserMeeting();
        return bonusTypeDao.findByDesc(church.getId(),meetings.getId(),desc,evaluationType);
    }

    @Override
    @Transactional
    public void updateBonusType(BonusType bonusType) {
        Church church           = userServices.getLogInUserChurch();
        Meetings meetings       = userServices.getLogInUserMeeting();
        bonusType.setChurch(church);
        bonusType.setMeetings(meetings);
        bonusTypeDao.updateBonusType(bonusType);
    }
}
