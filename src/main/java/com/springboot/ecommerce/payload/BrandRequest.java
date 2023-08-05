package com.springboot.ecommerce.payload;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Api(value = "Brand model information")
@Data
public class BrandRequest {
    @ApiModelProperty(value = "Brand name")
    @NotEmpty
    private String name;
}
