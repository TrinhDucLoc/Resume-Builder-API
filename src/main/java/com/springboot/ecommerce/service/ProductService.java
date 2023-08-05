package com.springboot.ecommerce.service;

import com.springboot.ecommerce.dto.BrandResponse;
import com.springboot.ecommerce.dto.PostResponse;
import com.springboot.ecommerce.dto.ProductPagingResponse;
import com.springboot.ecommerce.dto.ProductResponse;
import com.springboot.ecommerce.payload.BrandRequest;
import com.springboot.ecommerce.payload.ProductRequest;

import java.util.List;

public interface ProductService {
    ProductResponse createProduct(ProductRequest productRequest, Long categoryId);

    List<ProductResponse> getProductByCategoryId(Long categoryId);

    ProductResponse getProductById(Long productId);

    ProductResponse updateProductById(Long productId, ProductRequest productRequest);

    void deleteProductById(Long productId);

//    PostResponse getAllPosts(int pageNo, int pageSize, String sortBy, String sortDir);
    ProductPagingResponse getAllProducts(int pageNo, int pageSize, String sortBy, String sortDir);

    List<ProductResponse> getAllProductsNoFilter();
}
