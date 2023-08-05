package com.springboot.ecommerce.payload;

import com.springboot.ecommerce.dto.BrandResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.util.Set;


@Api(value = "Category model information")
@Data
public class CategoryRequest {
//    @ApiModelProperty(value = "Category id")
//    private Long id;
    @ApiModelProperty(value = "Category name")
    @NotEmpty
    private String name;

//    @ApiModelProperty(value = "Ecommerce Brands")
//    private Set<BrandResponse> brandResponses;
}
