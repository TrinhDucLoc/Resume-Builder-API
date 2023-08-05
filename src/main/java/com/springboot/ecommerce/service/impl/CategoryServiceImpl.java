package com.springboot.ecommerce.service.impl;

import com.springboot.ecommerce.dto.CategoryResponse;
import com.springboot.ecommerce.entity.Category;
import com.springboot.ecommerce.exception.ResourceNotFoundException;
import com.springboot.ecommerce.payload.CategoryRequest;
import com.springboot.ecommerce.repository.CategoryRepository;
import com.springboot.ecommerce.service.CategoryService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;

    private final ModelMapper modelMapper;

    public CategoryServiceImpl(CategoryRepository categoryRepository, ModelMapper modelMapper) {
        this.categoryRepository = categoryRepository;
        this.modelMapper = modelMapper;
    }


    @Override
    public CategoryResponse createCategory(CategoryRequest categoryRequest){
//        Convert DTO Request to entity
        Category category = modelMapper.map(categoryRequest, Category.class);
        Category newCategory = categoryRepository.save(category);
//        Convert entity to DTO Response
        return modelMapper.map(newCategory, CategoryResponse.class);
    }

    @Override
    public List<CategoryResponse> getAllCategory(){
        List<Category> categories = categoryRepository.findAll();
        return categories.stream().map(category -> modelMapper.map(category, CategoryResponse.class))
                                                              .collect(Collectors.toList());
    }

    @Override
    public CategoryResponse getCategoryById(Long id){
        Category category = categoryRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Category", "id", id));
        return modelMapper.map(category, CategoryResponse.class);
    }

    @Override
    public CategoryResponse updateCategoryById(Long id, CategoryRequest categoryRequest){
//        get category by id from the database
        Category category = categoryRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Category", "id", id));
//        set variable for columns
        category.setName(categoryRequest.getName());
//        save entity to database
        Category updateCategory = categoryRepository.save(category);
//        mapper entity to DTO Response
        return modelMapper.map(updateCategory, CategoryResponse.class);
    }

    @Override
    public void deleteCategoryById(Long id){
        Category category = categoryRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Category", "id", id));
        categoryRepository.delete(category);
    }
}
