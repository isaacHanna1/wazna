package com.watad.services;

import com.watad.dao.EventTypeDao;
import com.watad.entity.EventType;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EventTypeServicesImp implements EventTypeServices{
    private final EventTypeDao eventTypeDao;
    private final UserServices userServices;

    public EventTypeServicesImp(EventTypeDao eventTypeDao, UserServices userServices) {
        this.eventTypeDao = eventTypeDao;
        this.userServices = userServices;
    }

    @Override
    public List<EventType> findAll() {
            int churchId    = userServices.getLogInUserChurch().getId();
            int meetingId   = userServices.getLogInUserChurch().getId();
            return eventTypeDao.findAll(churchId,meetingId);
    }
}
