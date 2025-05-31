package com.watad.dao;

import com.watad.entity.EventDetail;

import java.util.List;

public interface EventDao {

    List<EventDetail> findAllActiveEvent(int curch_id , int meeting_id , int sprint_id);
}
