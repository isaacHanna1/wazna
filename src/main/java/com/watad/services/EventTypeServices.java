package com.watad.services;

import com.watad.entity.EventType;
import org.springframework.stereotype.Service;

import java.util.List;

public interface EventTypeServices {

    public List<EventType> findAll();
}
