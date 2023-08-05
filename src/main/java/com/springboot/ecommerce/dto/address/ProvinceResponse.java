package com.springboot.ecommerce.dto.address;

import io.swagger.annotations.Api;
import lombok.Data;

@Api(value = "Province model information")
@Data
public class ProvinceResponse {
    private String fullName;
}
