package com.watad.services;

import com.watad.dao.MeetingDao;
import com.watad.entity.Meetings;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MeetingServiceImp implements MeetingService{

    private  final MeetingDao  meetingDao;

    public MeetingServiceImp(MeetingDao meetingDao) {
        this.meetingDao = meetingDao;
    }
    @Override
    public List<Meetings> findAll() {
        return meetingDao.findAll();
    }

    @Override
    @Transactional
    public Meetings createMeating(Meetings meetings) {
        return meetingDao.createMeating(meetings);
    }
}
