package com.watad.controller;


import com.watad.entity.EventDetail;
import com.watad.services.EventService;
import com.watad.services.EventTypeServices;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/event")
public class EventController {

    private final EventTypeServices eventTypeServices;

    private final EventService eventService;

    public EventController(EventTypeServices eventTypeServices, EventService eventService) {
        this.eventTypeServices = eventTypeServices;
        this.eventService = eventService;
    }

    @GetMapping("/create")
    public String getViewOfCreateEvent(Model model){
        EventDetail event = new EventDetail();
        model.addAttribute("event",event);
        model.addAttribute("EventsType",eventTypeServices.findAll());
        return    "createEvent";
    }

    @PostMapping("/add")
    public String SaveEvent(EventDetail eventDetail ,
                            @RequestParam(value = "image", required = false) MultipartFile image , Model model){
        eventService.createEvent(eventDetail , image);
        EventDetail event = new EventDetail();
        model.addAttribute("event",event);
        model.addAttribute("EventsType",eventTypeServices.findAll());
        return    "createEvent";
    }


}
