package com.watad.dao;

import com.watad.entity.SprintData;

public interface SprintDataDao {

    SprintData getSprintDataByIsActive(int churchId , int meetingId);
}
