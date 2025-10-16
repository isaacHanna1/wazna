package com.watad.services;

import com.watad.Common.Util;
import com.watad.dao.ProfileDao;
import com.watad.dao.UserDao;
import com.watad.dto.ProfileDtlDto;
import com.watad.entity.Profile;
import com.watad.entity.User;
import com.watad.exceptions.PhomeNumberAlreadyException;
import com.watad.exceptions.ProfileException;
import com.watad.mapper.ProfileDtlMapper;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
    @Transactional(readOnly = false , rollbackFor = Exception.class)
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
        // userDao.saveUser(user);

        user.setProfile(profile);
        // The logic for save file
        if(image !=null && !image.isEmpty()) {
            String fileName = saveProfileImage(image);
            profile.setProfileImagePath(fileName);
        }
        profile.setJoinDate(LocalDateTime.now());
        profile.setImageUrl("");
        profile.setUser(user);
        profile.getFirstName().trim();
        profile.getLastName().trim();
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
        dto.setGender(profile.getGender().toString());
        dto.setPhone(profile.getPhone());
        dto.setBirthday(profile.getBirthday());
        dto.setAddress(profile.getAddress());
        dto.setFatherPeriest(profile.getPriest().getName());
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
        dto.setEmail(profile.getEmail());
        return dto;
    }

    @Override
    public ProfileDtlDto getProfileData(int userId) {
        User user         = userServices.findUserById(userId);
        int theId         = user.getProfile().getId();
        Profile profile   =  profileDao.getProfileById(theId);
        ProfileDtlDto dto = new ProfileDtlDto();
        dto.setId(theId);
        dto.setFirstName(profile.getFirstName());
        dto.setLastName(profile.getLastName());
        dto.setGender(profile.getGender().toString());
        dto.setPhone(profile.getPhone());
        dto.setBirthday(profile.getBirthday());
        dto.setAddress(profile.getAddress());
        dto.setFatherPeriest(profile.getPriest().getName());
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
        System.out.println("get the email data -> "+dto.getEmail());
        dto.setEmail(profile.getEmail());
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
    @Transactional
    public void editPrfofile(Profile profile , MultipartFile image , int id ) throws IOException {
        Profile existsOne = profileDao.getProfileById(id);
        String old_image = UPLOAD_DIR +"/"+existsOne.getProfileImagePath();
        LocalDateTime localDateTime = existsOne.getJoinDate();
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
//        User user = existsOne.getUser();
//        user.setUserName(profile.getPhone());
//        profile.setUser(user);
        profile.setJoinDate(localDateTime);
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

    @Override
    public List<ProfileDtlDto> findByUserPhoneOrUserName(String keyword) {
        List<ProfileDtlDto> listOfProfile = new ArrayList<>();
        if(!keyword.isEmpty()) {
            int churchId = userServices.getLogInUserChurch().getId();
            int meetingId = userServices.getLogInUserMeeting().getId();
            listOfProfile = profileDao.findByUserPhoneOrUserName (keyword, churchId, meetingId);
        }
        return  listOfProfile;
    }



    private void deleteOldFile(String filePath) {
        File file = new File(filePath);
        if (file.exists()) {
            file.delete();
        }
    }

    @Override
    public List<ProfileDtlDto> findAllByFilterPaginated(int profileId , String status, String gender, int pageNum, int pageSize) {
       return  profileDao.findAllByFilterPaginated( profileId ,status,gender,pageNum,pageSize);
    }

    @Override
    public int getTotalPagesByFilter(String status, String gender, int pageSize , int profileId) {
        return profileDao.getTotalPagesByFilter(status,gender,pageSize , profileId);
    }

    @Override
    public List<ProfileDtlDto> findProfileByNameOrPhone(String keyword, int churchId, int meetingId) {
        return  profileDao.findProfileByNameOrPhone(keyword,churchId,meetingId);
    }

    public Profile getEditableProfile(int profileId , HttpServletRequest request){
        int CurrentProfileId   = 0;
        Profile profile        = null;
        String  userName = (String)request.getSession().getAttribute("userName");
        System.out.println("The user name is "+userName);
        if(userName != null && !userName.isEmpty()){
            System.out.println("I`m in the session ");
            User user = userServices.findByUserNameForLogin(userName).orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + userName));
            CurrentProfileId = user.getProfile().getId();
            profile            = user.getProfile();
        }else {
            System.out.println("I`m in normal flow  ");
            profile           = getProfileById(profileId);
            CurrentProfileId = profile.getId();
        }
        if(CurrentProfileId != profileId){
            throw new ProfileException("Nice try, but you can only edit your own account.");
        }
        return profile;
    }

}
