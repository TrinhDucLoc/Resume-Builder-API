package com.springboot.ecommerce.service.impl;

import com.springboot.ecommerce.entity.Category;
import com.springboot.ecommerce.entity.FileDB;
import com.springboot.ecommerce.entity.Product;
import com.springboot.ecommerce.exception.FileNotFoundException;
import com.springboot.ecommerce.exception.ResourceNotFoundException;
import com.springboot.ecommerce.repository.FileDBRepository;
import com.springboot.ecommerce.service.FileStoreService;
import org.apache.commons.lang3.RandomStringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.*;
import java.util.Optional;
import java.util.stream.Stream;

@Service
public class FileStoreServiceImpl implements FileStoreService {
    private final FileDBRepository fileDBRepository;
    private final ModelMapper modelMapper;

    public FileStoreServiceImpl(FileDBRepository fileDBRepository, ModelMapper modelMapper) {
        this.fileDBRepository = fileDBRepository;
        this.modelMapper = modelMapper;
    }

    public FileDB store(MultipartFile file) throws IOException {
//        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
//        String fileName = file.getOriginalFilename();

//        Save image into folder
        Path uploadPath = Paths.get("Image-Upload");

        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        String fileCode = RandomStringUtils.randomAlphanumeric(8);

        InputStream inputStream = file.getInputStream();
        Path filePath = uploadPath.resolve(fileCode + "-" + file.getOriginalFilename());
        CopyOption copyOption = StandardCopyOption.REPLACE_EXISTING;
//        Save inputStream into filePath with CopyOption
        Files.copy(inputStream, filePath, copyOption);

//         Save image into database
        FileDB FileDB = new FileDB(file.getOriginalFilename(), file.getContentType(), file.getBytes());

        return fileDBRepository.save(FileDB);
    }

    public Stream<FileDB> getAllFiles() {
        return fileDBRepository.findAll().stream();
    }

    public FileDB getFile(String id) {
        return fileDBRepository.findById(id).get();
    }

    public void deleteFileById(String id){
        FileDB fileDB = fileDBRepository.findById(id).orElseThrow(
                () -> new FileNotFoundException(id)
        );
        fileDBRepository.delete(fileDB);
    }
}
