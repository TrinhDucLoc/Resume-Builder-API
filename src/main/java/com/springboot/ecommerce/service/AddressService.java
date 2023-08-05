package com.springboot.ecommerce.service;

import com.springboot.ecommerce.dto.address.DistrictResponse;
import com.springboot.ecommerce.dto.address.ProvinceResponse;
import com.springboot.ecommerce.dto.address.WardResponse;

import java.util.List;

public interface AddressService {
//    Get all provinces
    List<ProvinceResponse> getAllProvince();
//    Get list districts by province name
    List<DistrictResponse> getDistrictByNameProvince(String fullName);
//    Get list wards by district name
    List<WardResponse> getWardByDistrictName(String fullName);
}
