package com.watad.services;

import com.watad.dao.ProfileDao;
import com.watad.dao.UserDao;
import com.watad.entity.FamilyInfo;
import com.watad.entity.Profile;
import com.watad.entity.User;
import com.watad.exceptions.PhomeNumberAlreadyException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class RegisterChildServicesImp implements RegisterChildServices{

    private final UserDao userDao;
    private final PasswordEncoder passwordEncoder;
    private final RoleService roleService;
    private  final String UPLOAD_DIR = "./uploads/profile_pic";
    private final ProfileDao profileDao;

    public RegisterChildServicesImp(UserDao userDao, PasswordEncoder passwordEncoder, RoleService roleService, ProfileDao profileDao) {
        this.userDao = userDao;
        this.passwordEncoder = passwordEncoder;
        this.roleService = roleService;
        this.profileDao = profileDao;
    }

    @Override
    @Transactional
    public void registerChild(Profile profile, MultipartFile image) throws IOException {
        User user = profile.getUser();
        user.setUserName(user.getUserName());
        String rawPassword          = profile.getUser().getPassword();
        System.out.println("the Raw Password is "+rawPassword);
        String encodedPassword      = passwordEncoder.encode(rawPassword);
        System.out.println("the Raw encodedPassword is "+encodedPassword);
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
        profile.setJoinDate(LocalDateTime.now());
        profile.setImageUrl("");
        profile.setUser(user);
        profile.getFirstName().trim();
        profile.getLastName().trim();

        FamilyInfo familyInfo = profile.getFamilyInfo();
        familyInfo.setProfile(profile);
        familyInfo.setFatherName(profile.getFamilyInfo().getFatherName());
        familyInfo.setFatherTelephone(profile.getFamilyInfo().getFatherTelephone());
        familyInfo.setFatherExists(profile.getFamilyInfo().getFatherExists());
        familyInfo.setMotherName(profile.getFamilyInfo().getMotherName());
        familyInfo.setMotherTelephone(profile.getFamilyInfo().getMotherTelephone());
        familyInfo.setMotherExists(profile.getFamilyInfo().getMotherExists());

        profileDao.saveProfile(profile);
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
}
