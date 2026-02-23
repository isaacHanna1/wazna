package com.watad.controller;

import com.watad.services.EventService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller

public class HomeController {

    private final EventService eventService;

    public HomeController(EventService eventService) {
        this.eventService = eventService;
    }

    @GetMapping({"/", "/home"})
    public String showHome(Model model) {
        model.addAttribute("eventList", eventService.findAllActiveEvent(1));
        return "home";
    }
}
