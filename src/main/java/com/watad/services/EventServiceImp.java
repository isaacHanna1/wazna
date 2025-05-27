package com.watad.services;

import com.watad.dao.EventDao;
import com.watad.entity.EventDetail;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public  class EventServiceImp implements EventService{


    private final EventDao eventDao;

    public EventServiceImp(EventDao eventDao) {
        this.eventDao = eventDao;
    }

    @Override
    public List<EventDetail> findAllActiveEvent() {
        return eventDao.findAllActiveEvent();
    }
}
