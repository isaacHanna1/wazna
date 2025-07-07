package com.watad.dao;

import com.watad.entity.Meetings;

import java.time.LocalDate;
import java.util.List;

public interface MeetingDao {

    List<Meetings> findAll();
}
