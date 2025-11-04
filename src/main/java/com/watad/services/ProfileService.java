package com.watad.services;

import com.watad.dto.ProfileDtlDto;
import com.watad.entity.Profile;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public interface ProfileService {

    List<ProfileDtlDto> findAll();
    public void saveProfile(Profile profile , MultipartFile image) throws IOException;
    public ProfileDtlDto getProfileById();
    public ProfileDtlDto getProfileData(int id ); // when i need to view the data of user i will use this
    public  Profile getProfileById(int id );
    public  void editPrfofile(Profile profile , MultipartFile image , int id )  throws IOException;
    public  void editProfileForChild(Profile profile , MultipartFile image , int id )  throws IOException;
    public String getProfileImageName(int profile_id);
    List<ProfileDtlDto> findByUserPhone(String phone);
    List<ProfileDtlDto> findByUserPhoneOrUserName(String keyword);
    List<ProfileDtlDto> findAllByFilterPaginated(int profileId,String status , String gender , int pageNum , int pageSize,String serviceClass);
    public int getTotalPagesByFilter(String status, String gender, int pageSize , int profileId ,String serviceClass);
    public List<ProfileDtlDto> findProfileByNameOrPhone(String keyword , int churchId , int meetingId);
    public Profile getEditableProfile(int profileId , HttpServletRequest request);
    void deleteProfileById(int id);
}
