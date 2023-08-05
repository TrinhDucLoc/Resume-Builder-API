package com.springboot.ecommerce.service.impl;

import com.springboot.ecommerce.dto.BrandResponse;
import com.springboot.ecommerce.dto.CategoryResponse;
import com.springboot.ecommerce.dto.address.DistrictResponse;
import com.springboot.ecommerce.dto.address.ProvinceResponse;
import com.springboot.ecommerce.dto.address.WardResponse;
import com.springboot.ecommerce.entity.Brand;
import com.springboot.ecommerce.entity.Category;
import com.springboot.ecommerce.entity.address.District;
import com.springboot.ecommerce.entity.address.Province;
import com.springboot.ecommerce.entity.address.Ward;
import com.springboot.ecommerce.repository.address.DistrictRepository;
import com.springboot.ecommerce.repository.address.ProvinceRepository;
import com.springboot.ecommerce.repository.address.WardRepository;
import com.springboot.ecommerce.service.AddressService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AddressServiceImpl implements AddressService {
    private final ProvinceRepository provinceRepository;
    private final DistrictRepository districtRepository;
    private final WardRepository wardRepository;
    private final ModelMapper modelMapper;

    public AddressServiceImpl(ProvinceRepository provinceRepository, DistrictRepository districtRepository, WardRepository wardRepository, ModelMapper modelMapper) {
        this.provinceRepository = provinceRepository;
        this.districtRepository = districtRepository;
        this.wardRepository = wardRepository;
        this.modelMapper = modelMapper;
    }

    public List<ProvinceResponse> getAllProvince(){
        List<Province> provinces = provinceRepository.findAll();
        return provinces.stream().map(province -> modelMapper.map(province, ProvinceResponse.class))
                .collect(Collectors.toList());
    }


//    @Override
//    public List<BrandResponse> getBrandByCategoryId(Long categoryId){
//        // retrieve comments by postId
//        List<Brand> brands = brandRepository.findByCategoryId(categoryId);
//
//        // convert list of comment entities to list of comment dto's
//        return brands.stream().map(brand -> modelMapper.map(brand, BrandResponse.class))
//                .collect(Collectors.toList());
//    }

    @Override
    public List<DistrictResponse> getDistrictByNameProvince(String fullName){
//        retrieve provinceId by provinceName
        Province province = provinceRepository.findByFullName(fullName);

        Long provinceId = province.getId();
//        retrieve district by provinceId
        List<District> districts = districtRepository.findByProvinceId(provinceId);
//        return list district entities to dto
        return districts.stream().map(district -> modelMapper.map(district, DistrictResponse.class))
                .collect(Collectors.toList());

    }

    @Override
    public List<WardResponse> getWardByDistrictName(String fullName){
//        retrieve provinceId by provinceName
        District district = districtRepository.findByFullName(fullName);

//        Long provinceId = province.getId();
//        retrieve district by provinceId
//        List<District> districts = districtRepository.findByProvinceId(provinceId);
        List<Ward> wards = wardRepository.findByDistrictId(district.getId());
//        return list district entities to dto
        return wards.stream().map(ward -> modelMapper.map(ward, WardResponse.class))
                .collect(Collectors.toList());

    }
}
