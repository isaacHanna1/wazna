package com.watad.services;

import com.watad.entity.EventDetail;

import java.util.List;

public interface EventService {


    public void createEvent(EventDetail eventDetail);
    List<EventDetail> findAllActiveEvent();
}
