package com.watad.services;

import com.watad.entity.Profile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface RegisterChildServices {

    void registerChild(Profile profile , MultipartFile image) throws IOException;
}
