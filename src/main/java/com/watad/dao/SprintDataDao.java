package com.watad.dao;

import com.watad.entity.SprintData;

import java.util.List;

public interface SprintDataDao {

    SprintData getSprintDataByIsActive(int churchId , int meetingId);
    List<SprintData> findAll(int churchId , int meetingId);

}
