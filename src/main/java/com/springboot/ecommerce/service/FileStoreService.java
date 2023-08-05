package com.springboot.ecommerce.service;

import com.springboot.ecommerce.entity.FileDB;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.stream.Stream;

public interface FileStoreService {
//    Upload File
    public FileDB store(MultipartFile file) throws IOException;
//    Get all file
    Stream<FileDB> getAllFiles();
//    Get file by id
    FileDB getFile(String id);
//    Update file by id

//    Delete file by id
    void deleteFileById(String id);
}
