package com.watad.controller;

import com.watad.dto.MeetingDto;
import com.watad.entity.Church;
import com.watad.entity.Meetings;
import com.watad.services.MeetingService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class MeetingRestController {

    private final MeetingService meetingService;

    public MeetingRestController(MeetingService meetingService) {
        this.meetingService = meetingService;
    }

    @GetMapping("/meeting/{churchId}")
    public List<MeetingDto> getMeetingByChurchId(@PathVariable int churchId){
        return  meetingService.findByChurchId(churchId);
    }
}
