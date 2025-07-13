package com.watad.services;

import com.watad.entity.*;

import java.util.List;
import java.util.Optional;

public interface UserServices {

    void saveUser(User user);
    User findUserById(int id );
    Optional<User> findByUserNameForLogin(String userName);
    boolean existsByPhone(String phone);
    User logedInUser(); // return data of logged in user
    SprintData getActiveSprint();
    Meetings getLogInUserMeeting();
    Church getLogInUserChurch();
    Profile getLogedInUserProfile();
    List<User> findByRole(int role_id);

}
