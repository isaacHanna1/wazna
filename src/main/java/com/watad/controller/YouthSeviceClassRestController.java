package com.watad.controller;

import com.watad.dto.serviceClass.ServiceClassResponse;
import com.watad.services.YouthServiceClass;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class YouthSeviceClassRestController {

    private final YouthServiceClass youthServiceClass;

    @GetMapping("/service-classes")
    public ResponseEntity<List<ServiceClassResponse>> getServiceClass() {
        List<ServiceClassResponse> all = youthServiceClass.findAll();
        return ResponseEntity.ok(all);
    }
}
