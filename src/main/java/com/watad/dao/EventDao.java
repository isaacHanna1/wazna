package com.watad.dao;

import com.watad.entity.EventDetail;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface EventDao {

    public void createEvent(EventDetail eventDetail);
    List<EventDetail> findAllActiveEvent(int curch_id , int meeting_id , int sprint_id , int status);
    List<EventDetail> findAllCurrentSprintEvent(int curch_id , int meeting_id , int sprint_id , int status);
    EventDetail findById(int id );
    void edit(EventDetail eventDetail);
}
