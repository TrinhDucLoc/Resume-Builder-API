package com.springboot.ecommerce.controller;

import com.springboot.ecommerce.dto.CategoryResponse;
import com.springboot.ecommerce.dto.address.DistrictResponse;
import com.springboot.ecommerce.dto.address.ProvinceResponse;
import com.springboot.ecommerce.dto.address.WardResponse;
import com.springboot.ecommerce.service.AddressService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Api
@RestController
@RequestMapping("/api/address")
public class AddressController {
    private final AddressService addressService;

    public AddressController(AddressService addressService) {
        this.addressService = addressService;
    }

    //    Get all province
    @ApiOperation("Get All province REST API")
    @GetMapping
    public List<ProvinceResponse> getAllProvince(){
        return addressService.getAllProvince();
    }

//    Get list district by province name
    @ApiOperation("Get district by province name REST API")
    @GetMapping("/district/{province_name}")
    public List<DistrictResponse> getDistrictByNameProvince(@PathVariable(name = "province_name") String provinceName){
        return addressService.getDistrictByNameProvince(provinceName);
    }

    //    Get list ward by district name
    @ApiOperation("Get district by province name REST API")
    @GetMapping("/ward/{distristName}")
    public List<WardResponse> getWardByDistrictName(@PathVariable(name = "distristName") String districtName){
        return addressService.getWardByDistrictName(districtName);
    }
}
