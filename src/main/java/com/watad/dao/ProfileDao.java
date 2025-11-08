package com.watad.dao;

import com.watad.dto.ProfileDtlDto;
import com.watad.entity.Profile;

import java.util.List;

public interface ProfileDao {

    public List<Profile> findAll();
    public void saveProfile(Profile profile);
    public Profile getProfileById(int id);
    public void editprofile(Profile profile);
    String getPrfoileImageName(int id);
    List<ProfileDtlDto> findByUserPhone(String phone , int churchId , int meetingId);
    List<ProfileDtlDto> findByUserPhoneOrUserName(String keyword , int churchId , int meetingId);
    public List<ProfileDtlDto> findAllByFilterPaginated(int profileId ,String status , String gender , int pageNum , int pageSize,String serviceClass,String byRole);
    public int getTotalPagesByFilter(String status, String gender, int pageSize , int profileId , int churchId, int meetingId,String serviceClass,String byRole);
    public List<ProfileDtlDto> findProfileByNameOrPhone(String keyword , int churchId , int meetingId);
    public List<Profile> listProfilesOfMeeting(int meetingId);
    public void deleteProfileBy(int profileId);
}
