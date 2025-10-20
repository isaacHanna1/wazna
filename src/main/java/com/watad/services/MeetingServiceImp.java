package com.watad.services;

import com.watad.dao.MeetingDao;
import com.watad.dto.MeetingDto;
import com.watad.entity.Meetings;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MeetingServiceImp implements MeetingService{

    private  final MeetingDao  meetingDao;
    private final  UserServices userServices;

    public MeetingServiceImp(MeetingDao meetingDao, UserServices userServices) {
        this.meetingDao = meetingDao;
        this.userServices = userServices;
    }

    @Override
    public List<Meetings> findAll() {
        int churchId = userServices.getLogInUserChurch().getId();
        return meetingDao.findAll(churchId);
    }

    @Override
    @Transactional
    public Meetings createMeating(Meetings meetings) {
        return meetingDao.createMeating(meetings);
    }

    @Override
    public List<MeetingDto> findByChurchId(int churchId) {
        return  meetingDao.findByChurchId(churchId);
    }
}
