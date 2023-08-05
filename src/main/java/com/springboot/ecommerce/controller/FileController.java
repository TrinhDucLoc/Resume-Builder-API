package com.springboot.ecommerce.controller;

import com.springboot.ecommerce.dto.ResponseFile;
import com.springboot.ecommerce.dto.ResponseMessage;
import com.springboot.ecommerce.entity.FileDB;
import com.springboot.ecommerce.service.FileStoreService;
import io.swagger.annotations.Api;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Api
@RestController
@RequestMapping("/api/file")
public class FileController {
    private final FileStoreService fileStoreService;

    public FileController(FileStoreService fileStoreService) {
        this.fileStoreService = fileStoreService;
    }

    @PostMapping("/upload")
    public ResponseEntity<ResponseMessage> uploadFile(@RequestParam("file") MultipartFile file) {
        String message = "";
        try {
            fileStoreService.store(file);

            message = "Uploaded the file successfully: " + file.getOriginalFilename();
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message));
        } catch (Exception e) {
            message = "Could not upload the file: " + file.getOriginalFilename() + "!";
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseMessage(message));
        }
    }

    @PostMapping("/uploadMultipleFiles")
    public List<ResponseEntity<ResponseMessage>> uploadMultipleFiles(@RequestParam("files") MultipartFile[] files) {
        return Arrays.asList(files)
                .stream()
                .map(file -> uploadFile(file))
                .collect(Collectors.toList());
    }

    @GetMapping()
    public ResponseEntity<List<ResponseFile>> getListFiles() {
        List<ResponseFile> files = fileStoreService.getAllFiles().map(dbFile -> {
            String fileDownloadUri = ServletUriComponentsBuilder
                    .fromCurrentContextPath()
                    .path("/files/")
                    .path(dbFile.getId())
                    .toUriString();

            return new ResponseFile(
                    dbFile.getFileName(),
                    fileDownloadUri,
                    dbFile.getFileType(),
                    (long) dbFile.getData().length);
        }).collect(Collectors.toList());

        return ResponseEntity.status(HttpStatus.OK).body(files);
    }

//    @GetMapping("/{id}")
//    public ResponseEntity<byte[]> getFile(@PathVariable String id) {
//        FileDB fileDB = fileStoreService.getFile(id);
//
//        return ResponseEntity.ok()
//                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\""
//                        + fileDB.getFileName() + "\"")
//                .body(fileDB.getData());
//    }


    @GetMapping("/{id}")
    public ResponseEntity<FileDB> getFile(@PathVariable String id) {
        FileDB fileDB = fileStoreService.getFile(id);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\""
                        + fileDB.getFileName() + "\"")
                .body(fileDB);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteFileById(@PathVariable(name = "id") String id){
        fileStoreService.deleteFileById(id);

        return new ResponseEntity<>("File deleted successfully", HttpStatus.OK);
    }
}
