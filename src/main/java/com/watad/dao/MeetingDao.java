package com.watad.dao;

import com.watad.entity.Meetings;

import java.util.List;

public interface MeetingDao {

    List<Meetings> findAll();

}
