package com.springboot.ecommerce.dto.address;

import io.swagger.annotations.Api;
import lombok.Data;

@Api(value = "District model information")
@Data
public class DistrictResponse {
    private String fullName;
}
