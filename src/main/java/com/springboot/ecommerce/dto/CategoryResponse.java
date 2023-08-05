package com.springboot.ecommerce.dto;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Api(value = "Category model information")
@Data
public class CategoryResponse {
//    @ApiModelProperty(value = "Category id")
//    private Long id;

    @ApiModelProperty(value = "Category name")
    @NotEmpty
    private String name;
}
