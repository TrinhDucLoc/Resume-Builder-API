package com.springboot.ecommerce.controller;

import com.springboot.ecommerce.dto.CategoryResponse;
import com.springboot.ecommerce.payload.CategoryRequest;
import com.springboot.ecommerce.service.CategoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Api(value = "CURD REST APIs for category resources")
@RestController
@RequestMapping("/api/category")
public class CategoryController {
    private CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    //    Create category
    @ApiOperation("Create Category REST API")
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<CategoryResponse> createCategory(@Valid @RequestBody CategoryRequest categoryRequest){
        return new ResponseEntity<>(categoryService.createCategory(categoryRequest), HttpStatus.CREATED);
    }

//    Get all category
    @ApiOperation("Get All Category REST API")
    @GetMapping
    public List<CategoryResponse> getAllCategory(){
        return categoryService.getAllCategory();
    }
//    @GetMapping
//    public List<PostDto> getAllPosts(){
//        return postService.getAllPosts();
//    }

//    Get category by id
    @ApiOperation("Get Category By Id REST API")
    @GetMapping("/{id}")
    public ResponseEntity<CategoryResponse> getCategoryById(@PathVariable(name = "id") Long id){
        return ResponseEntity.ok(categoryService.getCategoryById(id));
    }

//    Update category by id
    @ApiOperation("Update Category By Id REST API")
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<CategoryResponse> updateCategoryById(@PathVariable(value = "id") Long id,
                                                               @Valid @RequestBody CategoryRequest categoryRequest){
        return new ResponseEntity<>(categoryService.updateCategoryById(id, categoryRequest), HttpStatus.CREATED);
    }

//    Delete category by id
    @ApiOperation("Delete Category By Id REST API")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCategoryById(@PathVariable(value = "id") Long id){
        categoryService.deleteCategoryById(id);
        return new ResponseEntity<>("Category deleted successfully", HttpStatus.OK);
    }

}
