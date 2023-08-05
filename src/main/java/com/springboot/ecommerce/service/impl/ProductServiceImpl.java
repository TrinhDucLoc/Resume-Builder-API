package com.springboot.ecommerce.service.impl;

import com.springboot.ecommerce.dto.CategoryResponse;
import com.springboot.ecommerce.dto.PostResponse;
import com.springboot.ecommerce.dto.ProductPagingResponse;
import com.springboot.ecommerce.dto.ProductResponse;
import com.springboot.ecommerce.entity.Category;
import com.springboot.ecommerce.entity.Post;
import com.springboot.ecommerce.entity.Product;
import com.springboot.ecommerce.exception.ResourceNotFoundException;
import com.springboot.ecommerce.payload.PostDto;
import com.springboot.ecommerce.payload.ProductRequest;
import com.springboot.ecommerce.repository.CategoryRepository;
import com.springboot.ecommerce.repository.ProductRepository;
import com.springboot.ecommerce.service.ProductService;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {

    private final CategoryRepository categoryRepository;

    private final ProductRepository productRepository;

    private final ModelMapper modelMapper;

    public ProductServiceImpl(CategoryRepository categoryRepository, ProductRepository productRepository, ModelMapper modelMapper) {
        this.categoryRepository = categoryRepository;
        this.productRepository = productRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public ProductResponse createProduct(ProductRequest productRequest, Long categoryId){
        //        Convert DTO Request to entity
        Product product = modelMapper.map(productRequest, Product.class);

        // retrieve category entity by id
        Category category = categoryRepository.findById(categoryId).orElseThrow(
                ()-> new ResourceNotFoundException("Category", "id", categoryId));

        // set category to product entity
        product.setCategory(category);

        //        save entity to repository
        Product newProduct = productRepository.save(product);

        //        Convert entity to DTO Response
        return modelMapper.map(newProduct, ProductResponse.class);
    }

    @Override
    public List<ProductResponse> getProductByCategoryId(Long categoryId){
//        retrieved product by category
        List<Product> products = productRepository.findByCategoryId(categoryId);

//        convert to list of product entity to product DTO response
        return products.stream().map(product -> modelMapper.map(product, ProductResponse.class))
                .collect(Collectors.toList());
    }

    @Override
    public ProductResponse getProductById(Long productId){
//        retrieved product by id
        Product product = productRepository.findById(productId).orElseThrow(
                () -> new ResourceNotFoundException("Product", "id", productId)
        );
////        retrieved category by id
//        Category category = categoryRepository.findById(categoryId).orElseThrow(
//                () -> new ResourceNotFoundException("Category", "id", categoryId)
//        );

//        return product by id
        return modelMapper.map(product, ProductResponse.class);
    }

    @Override
    public ProductResponse updateProductById(Long productId, ProductRequest productRequest){
//        retrieved product entity by id
        Product product = productRepository.findById(productId).orElseThrow(
                () -> new ResourceNotFoundException("Product", "id", productId)
        );

//        update product entity
        product.setName(productRequest.getName());
        product.setDescription(productRequest.getDescription());
        product.setQuantity(productRequest.getQuantity());
        product.setPrice(productRequest.getPrice());
//        product.setDiscountPercent(productRequest.getDiscount_percent());
        product.setEnable(productRequest.getEnable());
        product.setCountInStock(productRequest.getCountInStock());

//        save product entity to repository
        Product updateProduct = productRepository.save(product);

//        return product entity to DTO response
        return modelMapper.map(updateProduct, ProductResponse.class);
    }

    @Override
    public void deleteProductById(Long productId){
//        retrieve product entity by id
        Product product = productRepository.findById(productId).orElseThrow(
                () -> new ResourceNotFoundException("Product", "id", productId)
        );
//        delete product by id
        productRepository.delete(product);
    }

//    @Override
//    public PostResponse getAllPosts(int pageNo, int pageSize, String sortBy, String sortDir) {
//
//        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
//                : Sort.by(sortBy).descending();
//
//        // create Pageable instance
//        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
//
//        Page<Post> posts = postRepository.findAll(pageable);
//
//        // get content for page object
//        List<Post> listOfPosts = posts.getContent();
//
//        List<PostDto> content= listOfPosts.stream().map(post -> mapToDTO(post)).collect(Collectors.toList());
//
//        PostResponse postResponse = new PostResponse();
//        postResponse.setContent(content);
//        postResponse.setPageNo(posts.getNumber());
//        postResponse.setPageSize(posts.getSize());
//        postResponse.setTotalElements(posts.getTotalElements());
//        postResponse.setTotalPages(posts.getTotalPages());
//        postResponse.setLast(posts.isLast());
//
//        return postResponse;
//    }


    @Override
    public ProductPagingResponse getAllProducts(int pageNo, int pageSize, String sortBy, String sortDir){
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        // create Pageable instance
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);

        Page<Product> products = productRepository.findAll(pageable);

        // get content for page object

        List<Product> productList = products.getContent();

        List<ProductRequest> content = productList.stream().map(product -> modelMapper.map(product, ProductRequest.class)).collect(Collectors.toList());


        ProductPagingResponse productPagingResponse = new ProductPagingResponse();
        productPagingResponse.setContent(content);
        productPagingResponse.setPageNo(products.getNumber());
        productPagingResponse.setPageSize(products.getSize());
        productPagingResponse.setTotalElements(products.getTotalElements());
        productPagingResponse.setTotalPages(products.getTotalPages());
        productPagingResponse.setLast(productPagingResponse.isLast());

        return productPagingResponse;
    }

    @Override
    public List<ProductResponse> getAllProductsNoFilter(){
        List<Product> products = productRepository.findAll();
        return products.stream().map(product -> modelMapper.map(product, ProductResponse.class))
                .collect(Collectors.toList());
    }

}
