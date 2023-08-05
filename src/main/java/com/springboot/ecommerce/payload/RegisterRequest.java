package com.springboot.ecommerce.payload;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Data
public class RegisterRequest {
//    private String name;
@ApiModelProperty(value = "Blog post title")
@NotEmpty
@Size(min = 2, message = "Post title should have at least 2 characters")
    private String username;
    private String email;
    private String password;
//    private String address;
//    private String phoneNumber;
}
