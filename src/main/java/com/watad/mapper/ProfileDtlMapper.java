package com.watad.mapper;

import com.watad.dto.ProfileDtlDto;
import com.watad.entity.Profile;

import java.util.ArrayList;
import java.util.List;

public class ProfileDtlMapper {


    public static ProfileDtlDto convertProfileToDtlDto(Profile profile){
        ProfileDtlDto dtlDto = new ProfileDtlDto();
        dtlDto.setAddress(profile.getAddress());
        dtlDto.setBirthday(profile.getBirthday());
        dtlDto.setId(profile.getId());
        dtlDto.setPhone(profile.getPhone());
        dtlDto.setFirstName(profile.getFirstName());
        dtlDto.setLastName(profile.getLastName());
        dtlDto.setGender(profile.getGender().toString());
        dtlDto.setServiceStage(profile.getServiceStage().getDescription());
        dtlDto.setUserName(profile.getUser().getUserName());
        return dtlDto;
    }

    public static List<ProfileDtlDto> convertProfileToDtlDto(List<Profile> profiles){

        List<ProfileDtlDto> profileDtlDtoList = new ArrayList<>();
        for(Profile profile : profiles){
            ProfileDtlDto dtlDto = new ProfileDtlDto();
            dtlDto.setAddress(profile.getAddress());
            dtlDto.setBirthday(profile.getBirthday());
            dtlDto.setId(profile.getId());
            dtlDto.setPhone(profile.getPhone());
            dtlDto.setFirstName(profile.getFirstName());
            dtlDto.setLastName(profile.getLastName());
            dtlDto.setGender(profile.getGender().toString());
            dtlDto.setServiceStage(profile.getServiceStage().getDescription());
            dtlDto.setUserName(profile.getUser().getUserName());
            dtlDto.setFatherPeriest(profile.getFatherPeriest());
            profileDtlDtoList.add(dtlDto);
        }
        return profileDtlDtoList;
    }
}
