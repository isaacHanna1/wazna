package com.watad.services;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


@Service
public class YouthServiceClass {

    private final  UserServices userServices;

    public YouthServiceClass(UserServices userServices) {
        this.userServices = userServices;
    }

    public Map<Integer, String> getServicesClassByStage(int serviceStage){
        Map<Integer, String> stages = new LinkedHashMap<>();
        if(serviceStage == 1){ // collage
            stages.put(1, "أولى");
            stages.put(2, "تانية");
            stages.put(3, "تالتة");
            stages.put(4, "رابعة");
            stages.put(5, "خامسة وفيما فوق");
        }
        if(serviceStage == 2){ // prep
            stages.put(1, "أولى");
            stages.put(2, "تانية");
            stages.put(3, "تالتة");
        }
        if(serviceStage == 3){ // primary
            stages.put(3, "تالتة");
            stages.put(4, "رابعة");
            stages.put(5, "خامسة");
            stages.put(6, "سادسة");
        }
        if(serviceStage == 4){ // Egypt
            stages.put(1, "أولى");
            stages.put(2, "تانية");
            stages.put(3, "تالتة");
        }
        return stages;
    }

    public Map<Integer, String> getCurrentClassByStage(){
        int serviceStageId = userServices.getLogedInUserProfile().getServiceStage().getId();
        return this.getServicesClassByStage(serviceStageId);
    }

}
