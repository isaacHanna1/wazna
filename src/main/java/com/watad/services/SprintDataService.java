package com.watad.services;

import com.watad.dao.SprintDataDao;
import com.watad.entity.SprintData;

import java.util.List;

public interface SprintDataService {

    SprintData getSprintDataByIsActive(int churchId,int meetingId);
    public List<SprintData> findAll(int churchId, int meetingId);
}
