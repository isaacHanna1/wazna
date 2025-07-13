package com.watad.services;

import com.watad.dao.RoleDao;
import com.watad.dao.ServiceStageDao;
import com.watad.dao.UserDao;
import com.watad.entity.*;
import com.watad.enumValues.Gender;
import com.watad.security.CustomUserDetails;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserServicesImp implements UserServices{


    private final ServiceStageDao serviceStageDao;

    private final UserDao userDao;

    private final RoleDao roleDao;

    private final SprintDataService sprintDataService;


    public UserServicesImp(ServiceStageDao serviceStageDao, UserDao userDao , RoleDao roleDao ,SprintDataService sprintDataService) {
        this.serviceStageDao = serviceStageDao;
        this.userDao         = userDao;
        this.roleDao         = roleDao;
        this.sprintDataService = sprintDataService;
    }

    @Override
    public void saveUser(User user) {
        userDao.saveUser(user);
    }

    @Override
    public User findUserById(int id) {
        return userDao.findUserBYId(id).orElseThrow(()->new NoResultException());
    }

    @Override
    public Optional<User> findByUserNameForLogin(String userName) {
        return userDao.findByUserNameForLogin(userName);
    }

    @Override
    public boolean existsByPhone(String phone) {
            return userDao.existsByPhone(phone);
    }

    @Override
    public User logedInUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if(auth !=null && auth.getPrincipal() instanceof CustomUserDetails){
            CustomUserDetails customUserDetails = (CustomUserDetails) auth.getPrincipal();
            int id  =  customUserDetails.getId();
            return  findUserById(id);
        }
        return null;
    }

    public Profile getLogedInUserProfile(){
        return  logedInUser().getProfile();
    }


    @Override
    public List<User> findByRole(int role_id) {
        return  userDao.findByRoleId(role_id);
    }

    public Church getLogInUserChurch(){
        return  getLogedInUserProfile().getChurch();
    }
    public Meetings getLogInUserMeeting(){
        return getLogedInUserProfile().getMeetings();
    }
    public SprintData getActiveSprint(){

       int theMeetingId    = getLogInUserMeeting().getId();
       int theChurchId     = getLogInUserChurch().getId();
       return sprintDataService.getSprintDataByIsActive(theChurchId,theMeetingId);
    }


}
