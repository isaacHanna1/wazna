package com.watad.dao;

import com.watad.dto.MeetingDto;
import com.watad.entity.Meetings;
import org.springframework.data.jpa.repository.Meta;

import java.time.LocalDate;
import java.util.List;

public interface MeetingDao {

    List<Meetings> findAll();
    Meetings createMeating(Meetings meetings);
    List<MeetingDto> findByChurchId(int churchId);
}
