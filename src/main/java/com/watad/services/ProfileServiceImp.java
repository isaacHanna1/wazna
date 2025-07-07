package com.watad.services;

import com.watad.Common.Util;
import com.watad.dao.ProfileDao;
import com.watad.dao.UserDao;
import com.watad.dto.ProfileDtlDto;
import com.watad.entity.Profile;
import com.watad.entity.User;
import com.watad.exceptions.PhomeNumberAlreadyException;
import com.watad.mapper.ProfileDtlMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class ProfileServiceImp implements ProfileService {

    private final PasswordEncoder passwordEncoder;
    private final UserPointTransactionService userPointTransactionService;

    private  final String UPLOAD_DIR = "./uploads/profile_pic";
    private final ProfileDao  profileDao;
    private final RoleService roleService;
    private final UserDao     userDao;
    private final UserServices userServices;
    private final SprintDataService sprintDataService;
    private final YouthRankService youthRankService;
    public ProfileServiceImp(ProfileDao profileDao , RoleService roleService , UserDao userDao , PasswordEncoder passwordEncoder , UserServices userServices , UserPointTransactionService userPointTransactionService , SprintDataService sprintDataService , YouthRankService youthRankService) {
        this.profileDao         = profileDao;
        this.roleService        = roleService;
        this.userDao            = userDao;
        this.passwordEncoder    = passwordEncoder;
        this.userServices       = userServices;
        this.userPointTransactionService       = userPointTransactionService;
        this.sprintDataService = sprintDataService;
        this.youthRankService = youthRankService;

    }

    @Override
    public List<ProfileDtlDto> findAll() {
        return ProfileDtlMapper.convertProfileToDtlDto(profileDao.findAll());
    }
    @Override
    public void saveProfile(Profile profile , MultipartFile image) throws IOException{


        if(userDao.existsByPhone(profile.getPhone())){
            throw new PhomeNumberAlreadyException("Oops! It looks like this phone number is already saved. ");
        }

        User user = profile.getUser();
        user.setUserName(profile.getPhone());

        String rawPassword          = profile.getUser().getPassword();
        String encodedPassword      = passwordEncoder.encode(rawPassword);
        user.setPassword(encodedPassword);
        user.addRole(roleService.findByName("ROLE_YOUTH"));
        user.setLocked(false);
        user.setEnabled(false);
        user.setLastLogin(LocalDateTime.now());
        user.setProfile(profile);

        // The logic for save file
        if(image !=null && !image.isEmpty()) {
            String fileName = saveProfileImage(image);
            profile.setProfileImagePath(fileName);
        }
        profile.setUser(user);
        profile.setJoinDate(LocalDateTime.now());
        profile.setImageUrl("");
        userDao.saveUser(user);
        profileDao.saveProfile(profile);
    }

    @Override
    public ProfileDtlDto getProfileById() {
        User user = userServices.logedInUser();
        int theId = user.getProfile().getId();
        Profile profile=  profileDao.getProfileById(theId);
        ProfileDtlDto dto = new ProfileDtlDto();
        dto.setId(theId);
        dto.setFirstName(profile.getFirstName());
        dto.setLastName(profile.getLastName());
        dto.setGendre(profile.getGender().toString());
        dto.setPhone(profile.getPhone());
        dto.setBirthday(profile.getBirthday());
        dto.setAddress(profile.getAddress());
        dto.setFatherPeriest(profile.getFatherPeriest());
        dto.setChurchName(profile.getChurch().getChurchName());
        dto.setMeetingName(profile.getMeetings().getDescription());
        dto.setServiceStage(profile.getServiceStage().getDescription());
        dto.setImagePath(profile.getProfileImagePath());
        int age = Util.calculateAge(profile.getBirthday());
        int theSprintId = sprintDataService.getSprintDataByIsActive(profile.getChurch().getId(),profile.getMeetings().getId()).getId();
        double points = userPointTransactionService.getTotalPointsByProfileIdAndSprintId(theId,theSprintId);
        dto.setPoints(points);
        dto.setAge(age);
        dto.setRank(youthRankService.getSpecificYouthRank());
        return dto;
    }

    private String saveProfileImage(MultipartFile file) throws IOException {

        if (!file.getContentType().startsWith("image/")) {
            throw new IllegalArgumentException("Only image files are allowed.");
        }
        Files.createDirectories(Paths.get(UPLOAD_DIR));
        String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
        Path filePath = Paths.get(UPLOAD_DIR, fileName);
        Files.write(filePath, file.getBytes());
        return fileName;
    }

    public  Profile getProfileById(int id ){
        return profileDao.getProfileById(id);
    }

    @Override
    public void editPrfofile(Profile profile , MultipartFile image , int id ) throws IOException {
        Profile existsOne = profileDao.getProfileById(id);
        String old_image = UPLOAD_DIR +"/"+existsOne.getProfileImagePath();
        if (image != null && !image.isEmpty()){
            System.out.println("New image uploaded. Replacing old image.");
            System.out.println("the old image path is "+old_image);
            deleteOldFile(old_image);
            String imageName = saveProfileImage(image);
            profile.setProfileImagePath(imageName);
        }
        else{
            System.out.println("No new image uploaded. Keeping old image.");
            profile.setProfileImagePath(getProfileImageName(profile.getId()));
        }
        User user = existsOne.getUser();
        user.setUserName(profile.getPhone());
        profile.setUser(user);
        profile.setJoinDate(existsOne.getJoinDate());
        profileDao.editprofile(profile);

    }

    @Override
    public String getProfileImageName(int profile_id) {
        String imageName = profileDao.getPrfoileImageName(profile_id);
        if(imageName ==  null)
            return  "";
        return imageName;
    }

    @Override
    public List<ProfileDtlDto> findByUserPhone(String phone) {
        List<ProfileDtlDto> listOfProfile = new ArrayList<>();
        if(!phone.isEmpty()) {
            int churchId = userServices.getLogInUserChurch().getId();
            int meetingId = userServices.getLogInUserMeeting().getId();
             listOfProfile = profileDao.findByUserPhone(phone, churchId, meetingId);
        }
        return  listOfProfile;
    }
    private void deleteOldFile(String filePath) {
        File file = new File(filePath);
        if (file.exists()) {
            file.delete();
        }
    }
}
