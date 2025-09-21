package com.watad.services;

import com.watad.dao.EventDao;
import com.watad.entity.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Service
public  class EventServiceImp implements EventService{


    private final EventDao eventDao;
    private final UserServices userServices;
    private final SprintDataService sprintDataService;
    private final UploadFileServices uploadFileServices;
    @Value("${file.uploadEvent-dir}")
    private String uploadDir;
    public EventServiceImp(EventDao eventDao, UserServices userServices, SprintDataService sprintDataService, UploadFileServices uploadFileServices) {
        this.eventDao = eventDao;
        this.userServices = userServices;
        this.sprintDataService = sprintDataService;
        this.uploadFileServices = uploadFileServices;
    }

    @Override
    @Transactional
    public void createEvent(EventDetail eventDetail , MultipartFile file) {
        try {
            String fileName               = uploadFileServices.uploadFile(file, uploadDir);
            Church church                 = userServices.getLogInUserChurch();
            Meetings meetings             = userServices.getLogInUserMeeting();
            SprintData  sprintData        = userServices.getActiveSprint();
            eventDetail.setCurch(church);
            eventDetail.setMeetings(meetings);
            eventDetail.setSprintData(sprintData);
            eventDetail.setImageUrl(fileName);
            eventDetail.setEventActive(true);
            eventDao.createEvent(eventDetail);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public List<EventDetail> findAllActiveEvent(int status) {

        User user           = userServices.logedInUser();
        Profile theProfile  = user.getProfile();
        Meetings theMeeting = theProfile.getMeetings();
        int theMeetingId    = theMeeting.getId();
        int theChurchId     = theProfile.getChurch().getId();
        int theSprintId     = sprintDataService.getSprintDataByIsActive(theChurchId,theMeetingId).getId();
        List<EventDetail> events =  eventDao.findAllActiveEvent(theChurchId ,theChurchId ,theSprintId , status);
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

    @Override
    public List<EventDetail> findAllCurrentEvent(int status) {
        User user           = userServices.logedInUser();
        Profile theProfile  = user.getProfile();
        Meetings theMeeting = theProfile.getMeetings();
        int theMeetingId    = theMeeting.getId();
        int theChurchId     = theProfile.getChurch().getId();
        int theSprintId     = sprintDataService.getSprintDataByIsActive(theChurchId,theMeetingId).getId();
        List<EventDetail> events =  eventDao.findAllActiveEvent(theChurchId ,theChurchId ,theSprintId , status);
        return events;
    }

    @Override
    public EventDetail findById(int id) {
        return eventDao.findById(id);
    }

    @Override
    @Transactional
    public void edit(EventDetail eventDetail , MultipartFile file) throws IOException {
       if(file != null) {
           if (!file.isEmpty()) {
               uploadFileServices.deleteFile(uploadDir, eventDetail.getImageUrl());
               String fileName = uploadFileServices.uploadFile(file, uploadDir);
               eventDetail.setImageUrl(fileName);
           }
       }
        eventDetail.setCurch(userServices.getLogInUserChurch());
        eventDetail.setMeetings(userServices.getLogInUserMeeting());
        eventDetail.setSprintData(userServices.getActiveSprint());
        eventDao.edit(eventDetail);
    }

    @Override
    @Transactional
    public void editStatus(int id, boolean newStatus) throws IOException {
        EventDetail  eventDetail        =  findById(id);
        eventDetail.setEventActive(newStatus);
        edit(eventDetail,null);
    }
}
