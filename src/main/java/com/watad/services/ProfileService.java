package com.watad.services;

import com.watad.dto.ProfileDtlDto;
import com.watad.entity.Profile;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public interface ProfileService {

    List<ProfileDtlDto> findAll();
    public void saveProfile(Profile profile , MultipartFile image) throws IOException;
    public ProfileDtlDto getProfileById();
    public  Profile getProfileById(int id );
    public  void editPrfofile(Profile profile , MultipartFile image , int id )  throws IOException;
    public String getProfileImageName(int profile_id);
    List<ProfileDtlDto> findByUserPhone(String phone);
}
