package com.springboot.ecommerce.service;

import com.springboot.ecommerce.dto.BrandResponse;
import com.springboot.ecommerce.payload.BrandRequest;

import java.util.List;

public interface BrandService {
    BrandResponse createBrand(BrandRequest brandRequest, Long categoryId);

    List<BrandResponse> getBrandByCategoryId(Long categoryId);

    BrandResponse getBrandById(Long brandId, Long categoryId);

    BrandResponse updateBrandById(Long categoryId, Long brandId, BrandRequest brandRequest);

    void deleteBrandById(Long categoryId, Long brandId);
}
