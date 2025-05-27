package com.watad.services;

import com.watad.entity.EventDetail;

import java.util.List;

public interface EventService {

    List<EventDetail> findAllActiveEvent();
}
