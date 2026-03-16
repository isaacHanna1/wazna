package com.watad.controller;

import com.watad.dto.qrMeeting.QrMeetingDtoResponse;
import com.watad.services.QrCodeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/qr-codes")
@RequiredArgsConstructor
public class QrCodeRestController {

    private final QrCodeService qrCodeService;

    @GetMapping()
    public ResponseEntity<List<QrMeetingDtoResponse>> getQrCodeInRange(@RequestParam("date_from") LocalDate fromDate , @RequestParam("date_to") LocalDate toDate , @RequestParam("bonus_type_id") int bonusTypeId){
        List<QrMeetingDtoResponse> inRangeAndBonusType = qrCodeService.findInRangeAndBonusType(fromDate, toDate, bonusTypeId);
        return  ResponseEntity.ok(inRangeAndBonusType);
    }
}
