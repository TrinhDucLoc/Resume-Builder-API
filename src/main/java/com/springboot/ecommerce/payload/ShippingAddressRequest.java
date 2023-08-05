package com.springboot.ecommerce.payload;

import lombok.Data;

import javax.persistence.Column;
@Data
public class ShippingAddressRequest {
//    private Long id;
    private String fullName;
    private String phone;
    private String address;
}
