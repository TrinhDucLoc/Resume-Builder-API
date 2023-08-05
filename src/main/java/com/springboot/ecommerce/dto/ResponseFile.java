package com.springboot.ecommerce.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
public class ResponseFile {
    private final String fileName;
    private final String fileDownloadUri;
    private final String fileType;
    private final Long size;


    public ResponseFile(String fileName, String fileDownloadUri, String fileType, Long size) {
        this.fileName = fileName;
        this.fileDownloadUri = fileDownloadUri;
        this.fileType = fileType;
        this.size = size;
    }
}
