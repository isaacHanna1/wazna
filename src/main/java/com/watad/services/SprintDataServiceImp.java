package com.watad.services;

import com.watad.dao.SprintDataDao;
import com.watad.entity.SprintData;
import org.springframework.stereotype.Service;

@Service
public class SprintDataServiceImp implements  SprintDataService{


    private final SprintDataDao sprintDataDao;

    public SprintDataServiceImp(SprintDataDao sprintDataDao) {
        this.sprintDataDao = sprintDataDao;
    }

    @Override
    public SprintData getSprintDataByIsActive(int churchId , int meetingId) {
        return  sprintDataDao.getSprintDataByIsActive(churchId,meetingId);
    }
}
