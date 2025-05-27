package com.watad.services;

import com.watad.dao.SprintDataDao;
import com.watad.entity.SprintData;

public interface SprintDataService {


    SprintData getSprintDataByIsActive(int churchId,int meetingId);

}
