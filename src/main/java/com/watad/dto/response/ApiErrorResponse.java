package com.watad.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;


@Getter
@AllArgsConstructor
public class ApiErrorResponse {

    private String error ;
    private String message;
    private int status ;
    private Long timeStamp ;




}
