package com.watad.services;

import com.watad.entity.EventDetail;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface EventService {


    public void createEvent(EventDetail eventDetail , MultipartFile file);
    List<EventDetail> findAllActiveEvent(int status);
    List<EventDetail> findAllCurrentEvent(int status);
    EventDetail findById(int id );
    void edit(EventDetail eventDetail ,MultipartFile file) throws IOException;
    void editStatus(int id , boolean newStatus) throws IOException;
}
