package com.watad.services;

import com.watad.dao.UserDao;
import com.watad.entity.User;
import com.watad.security.CustomUserDetails;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service("customUserDetailsService")
public class CustomUserDetailsService implements UserDetailsService {


    private final UserServices userServices;

    public CustomUserDetailsService(UserServices userServices) {
        this.userServices = userServices;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = userServices.findByUserNameForLogin(username).orElseThrow(()-> new UsernameNotFoundException("User Not Found"));
        user.setLastLogin(LocalDateTime.now());
        userServices.saveUser(user);
        return new CustomUserDetails(user);

    }
}
