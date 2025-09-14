package com.watad.dao;

import com.watad.entity.EventType;

import java.util.List;

public interface EventTypeDao {

    List<EventType> findAll(int churchId , int meetingId);
}
