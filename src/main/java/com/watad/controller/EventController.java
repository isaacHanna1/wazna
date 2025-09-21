package com.watad.controller;


import com.watad.entity.EventDetail;
import com.watad.services.EventService;
import com.watad.services.EventTypeServices;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Controller
@RequestMapping("/event")
public class EventController {

    private final EventTypeServices eventTypeServices;

    private final EventService eventService;
    @Value("${file.uploadEvent-dir}")
    private String uploadDir;
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

    @GetMapping("/view")
    public String getViewOfEvent(Model model){
        List<EventDetail> allList = eventService.findAllCurrentEvent(3); //  3 meaning get all status of  events active and not active
        model.addAttribute("events",allList);
        return "allEvents";
    }


    @PostMapping("/changeStatus")
    public String changeStatus(@RequestParam int eventId , @RequestParam boolean currentStatus) throws IOException {
        eventService.editStatus(eventId,!currentStatus);
        return "redirect:/event/view";
    }

    @GetMapping("/find/{id}")
    public String findById(@PathVariable int id , Model model){
        model.addAttribute("EventsType",eventTypeServices.findAll());
        model.addAttribute("event",eventService.findById(id));
        return "eventEditView";
    }

    @PostMapping("/edit")
    public String edit(EventDetail eventDetail , @RequestParam(value = "image", required = false) MultipartFile image) throws IOException {
     eventService.edit(eventDetail,image);
     return "redirect:/event/view";
    }

    @GetMapping("/image/{fileName:.+}")
    @ResponseBody
    public ResponseEntity<Resource> getImage(@PathVariable String fileName ) throws IOException {
        Path file            = Paths.get(uploadDir).resolve(fileName);
        Resource resource    = new UrlResource(file.toUri());
        if(resource.exists() || resource.isReadable()){
            String contentType = Files.probeContentType(file);
            if (contentType == null) {
                contentType = "application/octet-stream";
            }
            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .body(resource);
        }else {
            return ResponseEntity.notFound().build();
        }
    }
}
