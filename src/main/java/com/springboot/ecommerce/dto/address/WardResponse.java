package com.springboot.ecommerce.dto.address;

import io.swagger.annotations.Api;
import lombok.Data;

@Api(value = "Ward model information")
@Data
public class WardResponse {
    private String fullName;
}
