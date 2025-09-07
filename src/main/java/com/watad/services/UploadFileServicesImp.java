package com.watad.services;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;
@Service
public class UploadFileServicesImp implements UploadFileServices{

    @Override
    public String uploadFile(MultipartFile file, String folderPath) throws IOException {
        String fileName ="";
            Files.createDirectories(Paths.get(folderPath));
                   fileName = generateFileName(file);
            Path file_Path  = Paths.get(folderPath, fileName);
            Files.write(file_Path, file.getBytes());
            return fileName;

    }

    @Override
    public String generateFileName(MultipartFile file) {
        return UUID.randomUUID()+"_"+file.getOriginalFilename();
    }

    @Override
    public boolean deleteFile(String path, String fileName) {
        String fullPath = path+fileName;
        File fileToDelete  = new File(fullPath);
        if(fileToDelete.exists()){
            fileToDelete.delete();
            return true;
        }
        return  false;
    }

}
