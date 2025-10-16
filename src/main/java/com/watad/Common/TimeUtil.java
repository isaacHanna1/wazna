package com.watad.Common;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Component
public class TimeUtil {
    @Value("${app.timezone}")
    private  String appTimeZone;

    public LocalDateTime now() {
        return LocalDateTime.now(ZoneId.of(appTimeZone));
    }

    public LocalDate now_localDate(){
        return LocalDate.now(ZoneId.of(appTimeZone));
    }
}
