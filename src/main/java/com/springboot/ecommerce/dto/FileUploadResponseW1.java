package com.springboot.ecommerce.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Data
public class FileUploadResponseW1 {
    private String fileName;
    private String downloadUri;
    private long size;

    // getters and setters are not shown for brevity

}