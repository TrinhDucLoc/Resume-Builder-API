package com.springboot.ecommerce.controller;

import com.springboot.ecommerce.dto.CategoryResponse;
import com.springboot.ecommerce.dto.PostResponse;
import com.springboot.ecommerce.dto.ProductPagingResponse;
import com.springboot.ecommerce.dto.ProductResponse;
import com.springboot.ecommerce.payload.ProductRequest;
import com.springboot.ecommerce.service.ProductService;
import com.springboot.ecommerce.utils.AppConstants;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Api
@RestController
@RequestMapping("/api")
public class ProductController {
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    //    create product
    @ApiOperation(value = "Create product REST API")
    @PostMapping("/product")
    public ResponseEntity<ProductResponse> createProduct(@Valid @RequestBody ProductRequest productRequest,
                                                          Long categoryId){
        return new ResponseEntity<>(productService.createProduct(productRequest, categoryId), HttpStatus.CREATED);
    }

    //    get product by category id
    @ApiOperation(value = "Get Product by Category Id REST API")
    @GetMapping("/product/category/{categoryId}/")
    public List<ProductResponse> getProductByCategoryId(@PathVariable(value = "categoryId") Long categoryId){
        return productService.getProductByCategoryId(categoryId);
    }

    //    get product by id
    @ApiOperation(value = "Get product by id REST API")
    @GetMapping("/product/{productId}")
    public ResponseEntity<ProductResponse> getProductById(@PathVariable(name = "productId") Long productId){
        return ResponseEntity.ok(productService.getProductById(productId));
    }

    //    update product
    @ApiOperation(value = "Update product by id REST API")
    @PutMapping("product/{productId}")
    public ResponseEntity<ProductResponse> updateProductById(@PathVariable(name = "productId") Long productId,
                                                             @Valid @RequestBody ProductRequest productRequest){
        return new ResponseEntity<>(productService.updateProductById(productId, productRequest), HttpStatus.OK);
    }

    //    delete product
    @ApiOperation(value = "Delete product by id REST API")
    @DeleteMapping("product/{productId}")
    public ResponseEntity<String> deleteProductById(@PathVariable(name = "productId") Long productId){
        productService.deleteProductById(productId);
        return ResponseEntity.ok("Product deleted successfully");
    }

    @ApiOperation(value = "Get All Products REST API")
    // get all posts rest api
    @GetMapping("/product")
    public ProductPagingResponse getAllProducts(
            @RequestParam(value = "pageNo", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER, required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_BY, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = AppConstants.DEFAULT_SORT_DIRECTION, required = false) String sortDir
    ){
        return productService.getAllProducts(pageNo, pageSize, sortBy, sortDir);
    }

    @ApiOperation("Get All Category REST API")
    @GetMapping("/products")
    public List<ProductResponse> getAllProductsNoFilter(){
        return productService.getAllProductsNoFilter();
    }
}
