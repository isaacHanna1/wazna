package com.watad.services;

import com.watad.entity.EventDetail;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface EventService {


    public void createEvent(EventDetail eventDetail , MultipartFile file);
    List<EventDetail> findAllActiveEvent();
}
