package com.watad.controller;


import com.watad.entity.EventDetail;
import com.watad.services.EventTypeServices;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/event")
public class EventController {

    private final EventTypeServices eventTypeServices;

    public EventController(EventTypeServices eventTypeServices) {
        this.eventTypeServices = eventTypeServices;
    }

    @GetMapping("/create")
    public String getViewOfCreateEvent(Model model){
        EventDetail event = new EventDetail();
        model.addAttribute("event",event);
        model.addAttribute("EventsType",eventTypeServices.findAll());
        return    "createEvent";
    }


}
