package com.springboot.ecommerce.dto;

import com.springboot.ecommerce.payload.PostDto;
import com.springboot.ecommerce.payload.ProductRequest;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductPagingResponse {
    private List<ProductRequest> content;
    private int pageNo;
    private int pageSize;
    private long totalElements;
    private int totalPages;
    private boolean last;
}
