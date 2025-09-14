package com.watad.services;

import com.watad.dao.EventDao;
import com.watad.entity.EventDetail;
import com.watad.entity.Meetings;
import com.watad.entity.Profile;
import com.watad.entity.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public  class EventServiceImp implements EventService{


    private final EventDao eventDao;
    private final UserServices userServices;
    private final SprintDataService sprintDataService;

    public EventServiceImp(EventDao eventDao, UserServices userServices, SprintDataService sprintDataService) {
        this.eventDao = eventDao;
        this.userServices = userServices;
        this.sprintDataService = sprintDataService;
    }

    @Override
    public void createEvent(EventDetail eventDetail) {
        eventDao.createEvent(eventDetail);
    }

    @Override
    public List<EventDetail> findAllActiveEvent() {

        User user           = userServices.logedInUser();
        Profile theProfile  = user.getProfile();
        Meetings theMeeting = theProfile.getMeetings();
        int theMeetingId    = theMeeting.getId();
        int theChurchId     = theProfile.getChurch().getId();
        int theSprintId     = sprintDataService.getSprintDataByIsActive(theChurchId,theMeetingId).getId();
        List<EventDetail> events =  eventDao.findAllActiveEvent(theChurchId ,theChurchId ,theSprintId);
        if (events.isEmpty()) {
            if (events == null || events.isEmpty()) {
                EventDetail defaultEvent = new EventDetail();
                defaultEvent.setTitle("NO Event Till Now");
                defaultEvent.setDescription("Wait Us For Coming Event");
                defaultEvent.setImageUrl("commingSoon.jpg");
                defaultEvent.setPrice(0);
                return List.of(defaultEvent);
            }
        }
        return events;
    }
}
