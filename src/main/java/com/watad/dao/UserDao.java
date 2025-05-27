package com.watad.dao;

import com.watad.entity.User;

import java.util.Optional;

public interface UserDao {

    public void saveUser(User User);
    Optional<User> findUserBYId(int id);
    public Optional<User> findByUserNameForLogin(String userName);
    boolean existsByPhone(String phone);

}
