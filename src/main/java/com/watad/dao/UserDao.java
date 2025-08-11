package com.watad.dao;

import com.watad.dto.RoleDto;
import com.watad.dto.UserCountsDto;
import com.watad.entity.Role;
import com.watad.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserDao {

    public void saveUser(User User);
    Optional<User> findUserBYId(int id);
    public Optional<User> findByUserNameForLogin(String userName);
    boolean existsByPhone(String phone);
    List<User> findByRoleId(int role_id);
    UserCountsDto getCountsInMeeting(int churchId , int meetingId);
    void activeOrDisactiveUser(User user);
    List<RoleDto> getUserRole(String userName);
    void updateUserRole(User user , Role role);
}
