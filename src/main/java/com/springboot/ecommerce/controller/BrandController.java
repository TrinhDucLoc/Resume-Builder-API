package com.springboot.ecommerce.controller;

import com.springboot.ecommerce.dto.BrandResponse;
import com.springboot.ecommerce.dto.CategoryResponse;
import com.springboot.ecommerce.payload.BrandRequest;
import com.springboot.ecommerce.payload.CategoryRequest;
import com.springboot.ecommerce.payload.CommentDto;
import com.springboot.ecommerce.service.BrandService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Api(value = "CURD REST APIs for brand resources")
@RestController
@RequestMapping("/api")
public class BrandController {
    private final BrandService brandService;

    public BrandController(BrandService brandService) {
        this.brandService = brandService;
    }

    //    Create brand
    @ApiOperation("Create Brand REST API")
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/category/{categoryId}/brand")
    public ResponseEntity<BrandResponse> createBrand(@Valid @RequestBody BrandRequest brandRequest,
                                                     @PathVariable(name = "categoryId") Long categoryId){
        return new ResponseEntity<>(brandService.createBrand(brandRequest, categoryId), HttpStatus.CREATED);
    }

//    Get all brand by categoryId
    @ApiOperation(value = "Get All Brands By Category ID REST API")
    @GetMapping("/category/{categoryId}/brand")
    public List<BrandResponse> getBrandByCategoryId(@PathVariable(value = "categoryId") Long categoryId){
        return brandService.getBrandByCategoryId(categoryId);
    }

//    Get brand by id
    @ApiOperation(value = "Get Single Brand By ID REST API")
    @GetMapping("/category/{categoryId}/brand/{brandId}")
    public ResponseEntity<BrandResponse> getBrandById(@PathVariable(value = "brandId") Long brandId,
                                                     @PathVariable(value = "categoryId") Long categoryId){
        BrandResponse brandResponse = brandService.getBrandById(brandId, categoryId);
        return new ResponseEntity<>(brandResponse, HttpStatus.OK);
    }

//    Update brand
    @ApiOperation(value = "Update Brand By Id REST API")
    @PutMapping("/category/{categoryId}/brand/{brandId}")
    public ResponseEntity<BrandResponse> updateBrandById(@PathVariable(value = "categoryId") Long categoryId,
                                                         @PathVariable(value = "brandId") Long brandId,
                                                         @Valid @RequestBody BrandRequest brandRequest){
        return new ResponseEntity<>(brandService.updateBrandById(categoryId, brandId, brandRequest), HttpStatus.OK);
    }

//    Delete Brand
    @ApiOperation(value = "Delete Brand By ID REST API")
    @DeleteMapping("/category/{categoryId}/brand/{brandId}")
    public ResponseEntity<String> deleteBrandById(@PathVariable(value = "categoryId") Long categoryId,
                                                  @PathVariable(value = "brandId") Long brandId){
        brandService.deleteBrandById(categoryId, brandId);
        return new ResponseEntity<>("Brand deleted successfully", HttpStatus.OK);
    }

}
