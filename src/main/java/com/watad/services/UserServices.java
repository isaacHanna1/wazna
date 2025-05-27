package com.watad.services;

import com.watad.entity.User;

import java.util.Optional;

public interface UserServices {

    void saveUser(User user);
    User findUserById(int id );
    Optional<User> findByUserNameForLogin(String userName);
    boolean existsByPhone(String phone);
    User logedInUser(); // return data of logged in user
}
