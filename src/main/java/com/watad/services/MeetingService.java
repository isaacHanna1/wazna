package com.watad.services;

import com.watad.entity.Meetings;

import java.util.List;

public interface MeetingService {

    List<Meetings> findAll();
    Meetings createMeating(Meetings meetings) ;
    }
