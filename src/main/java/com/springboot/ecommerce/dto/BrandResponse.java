package com.springboot.ecommerce.dto;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Api(value = "Brand model information")
@Data
public class BrandResponse {
    @ApiModelProperty(value = "Brand name")
    @NotEmpty
    private String name;
}
