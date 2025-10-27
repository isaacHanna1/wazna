package com.watad.services;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class CodeGeneratorServiceImp implements  CodeGeneratorService{


    private  final UserServices userServices;

    public CodeGeneratorServiceImp(UserServices userServices) {
        this.userServices = userServices;
    }

    @Override
    public String CodeGeneratorService() {
        int dioceseId = userServices.getLogInUserDiocese().getId();
        int churchId  = userServices.getLogInUserChurch().getId();
        int meetingId = userServices.getLogInUserMeeting().getId();

        // Generate shorter, barcode-friendly random part
        String randomPart = UUID.randomUUID()
                .toString()
                .replaceAll("[^A-Za-z0-9]", "") // remove non-alphanumeric characters
                .substring(0, 8)
                .toUpperCase();
        // New format: D + dioceseId + C + churchId + M + meetingId + randomPart
        String code        = "D" + dioceseId + "C" + churchId + "M" + meetingId +"CO"+ randomPart;

        return code;
    }
}
