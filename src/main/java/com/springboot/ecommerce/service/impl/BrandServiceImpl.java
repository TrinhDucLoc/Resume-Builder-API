package com.springboot.ecommerce.service.impl;

import com.springboot.ecommerce.dto.BrandResponse;
import com.springboot.ecommerce.entity.Brand;
import com.springboot.ecommerce.entity.Category;
import com.springboot.ecommerce.exception.BlogAPIException;
import com.springboot.ecommerce.exception.ResourceNotFoundException;
import com.springboot.ecommerce.payload.BrandRequest;
import com.springboot.ecommerce.repository.BrandRepository;
import com.springboot.ecommerce.repository.CategoryRepository;
import com.springboot.ecommerce.service.BrandService;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BrandServiceImpl implements BrandService {
    private final BrandRepository brandRepository;
    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper;

    public BrandServiceImpl(BrandRepository brandRepository, CategoryRepository categoryRepository, ModelMapper modelMapper) {
        this.brandRepository = brandRepository;
        this.categoryRepository = categoryRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public BrandResponse createBrand(BrandRequest brandRequest, Long categoryId){
//        Convert DTO Request to entity
        Brand brand = modelMapper.map(brandRequest, Brand.class);

        // retrieve category entity by id
        Category category = categoryRepository.findById(categoryId).orElseThrow(
                ()-> new ResourceNotFoundException("Category", "id", categoryId));

        // set category to brand entity
        brand.setCategory(category);

//        save brand entity to repository
        Brand newBrand = brandRepository.save(brand);

//        Convert entity to DTO Response
        return modelMapper.map(newBrand, BrandResponse.class);
    }

    @Override
    public List<BrandResponse> getBrandByCategoryId(Long categoryId){
        // retrieve comments by postId
        List<Brand> brands = brandRepository.findByCategoryId(categoryId);

        // convert list of comment entities to list of comment dto's
        return brands.stream().map(brand -> modelMapper.map(brand, BrandResponse.class))
                .collect(Collectors.toList());
    }

    @Override
    public BrandResponse getBrandById(Long brandId, Long categoryId){
        // retrieve category entity by id
        Category category = categoryRepository.findById(categoryId).orElseThrow(
                () -> new ResourceNotFoundException("Category", "id", categoryId));

        // retrieve brand by id
        Brand brand = brandRepository.findById(brandId).orElseThrow(
                () -> new ResourceNotFoundException("Brand", "id", brandId));

//        if(!comment.getPost().getId().equals(post.getId())){
//            throw new BlogAPIException(HttpStatus.BAD_REQUEST, "Comment does not belong to post");
//        }

        return modelMapper.map(brand, BrandResponse.class);
    }

    @Override
    public BrandResponse updateBrandById(Long categoryId, Long brandId, BrandRequest brandRequest){
        // retrieve category entity by id
        Category category = categoryRepository.findById(categoryId).orElseThrow(
                ()-> new ResourceNotFoundException("Category", "id", categoryId)
        );

        // retrieve brand by id
        Brand brand = brandRepository.findById(brandId).orElseThrow(
                ()-> new ResourceNotFoundException("Brand", "id", brandId));

        if(!brand.getCategory().getId().equals(category.getId())){
            throw new BlogAPIException(HttpStatus.BAD_REQUEST, "Brand does not belongs to category");
        }

//        Update parameter brand request to entity
        brand.setName(brandRequest.getName());

        Brand updateBrand = brandRepository.save(brand);
        return modelMapper.map(updateBrand, BrandResponse.class);
    }

    @Override
    public void deleteBrandById(Long categoryId, Long brandId){
        // retrieve category entity by id
        Category category = categoryRepository.findById(categoryId).orElseThrow(
                () -> new ResourceNotFoundException("Category", "id", categoryId)
        );

        // retrieve brand by id
        Brand brand = brandRepository.findById(brandId).orElseThrow(
                () -> new ResourceNotFoundException("Brand", "id", brandId)
        );

        if(!brand.getCategory().getId().equals(category.getId())){
            throw new BlogAPIException(HttpStatus.BAD_REQUEST, "Brand does not belongs to category");
        }

        brandRepository.delete(brand);
    }
}
