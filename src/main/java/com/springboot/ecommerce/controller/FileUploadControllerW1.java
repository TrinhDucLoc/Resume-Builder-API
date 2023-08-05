package com.springboot.ecommerce.controller;

import java.io.IOException;
import java.util.Objects;

import com.springboot.ecommerce.dto.FileUploadResponseW1;
import com.springboot.ecommerce.utils.FileDownLoadUtilW1;
import com.springboot.ecommerce.utils.FileUploadUtilW1;
import io.swagger.annotations.Api;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Api
@RestController
public class FileUploadControllerW1 {

    @PostMapping("/uploadFile")
    public ResponseEntity<FileUploadResponseW1> uploadFile(
            @RequestParam("file") MultipartFile multipartFile)
            throws IOException {

//        String fileName = StringUtils.cleanPath(Objects.requireNonNull(multipartFile.getOriginalFilename()));
        String fileName = multipartFile.getOriginalFilename();

        long size = multipartFile.getSize();

        String filecode = FileUploadUtilW1.saveFile(fileName, multipartFile);

        FileUploadResponseW1 response = new FileUploadResponseW1();
        response.setFileName(fileName);
        response.setSize(size);
        response.setDownloadUri("/downloadFile/" + filecode);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/downloadFile/{fileCode}")
    public ResponseEntity<?> downloadFile(@PathVariable("fileCode") String fileCode) {
        FileDownLoadUtilW1 downloadUtil = new FileDownLoadUtilW1();

        Resource resource = null;
        try {
            resource = downloadUtil.getFileAsResource(fileCode);
        } catch (IOException e) {
//            return ResponseEntity.internalServerError().build();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();

        }

        if (resource == null) {
            return new ResponseEntity<>("File not found", HttpStatus.NOT_FOUND);
        }

        String contentType = "application/octet-stream";
        String headerValue = "attachment; filename=\"" + resource.getFilename() + "\"";

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, headerValue)
                .body(resource);
    }
}