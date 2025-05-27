package com.watad.dao;

import com.watad.entity.Profile;

import java.util.List;

public interface ProfileDao {

    public List<Profile> findAll();
    public void saveProfile(Profile profile);
    public Profile getProfileById(int id);
}
