package com.springboot.ecommerce.service;

import com.springboot.ecommerce.dto.CategoryResponse;
import com.springboot.ecommerce.payload.CategoryRequest;

import java.util.List;

public interface CategoryService {
    CategoryResponse createCategory(CategoryRequest categoryRequest);

    List<CategoryResponse> getAllCategory();

    CategoryResponse getCategoryById(Long id);

    CategoryResponse updateCategoryById(Long id, CategoryRequest categoryRequest);

    void deleteCategoryById(Long id);
}
