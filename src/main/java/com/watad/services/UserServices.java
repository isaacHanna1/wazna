package com.watad.services;

import com.watad.dto.RoleDto;
import com.watad.dto.UserCountsDto;
import com.watad.entity.*;

import java.util.List;
import java.util.Optional;
import java.util.Set;

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
    UserCountsDto getCountsInMeeting();
    int activeOrDisactiveUser(boolean enabled ,String userName);
    List<RoleDto> getUserRoles(String userName);
    void updateUserRole(String userName , int role_id);
    String getCurrentUserRole();
}
