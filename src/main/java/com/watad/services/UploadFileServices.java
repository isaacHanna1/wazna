package com.watad.services;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface UploadFileServices {

    public String uploadFile(MultipartFile file , String folderPath) throws IOException;
    public String generateFileName(MultipartFile file);
    public boolean deleteFile(String path , String fileName);
}
