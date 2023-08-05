package com.springboot.ecommerce.payload;

import com.springboot.ecommerce.entity.Order;
import com.springboot.ecommerce.entity.Product;
import lombok.Data;

import javax.persistence.*;

@Data
public class OrderDetailRequest {
    private Long id;
    private String productName;
    private Float productCost;
    private Float quantity;
    private Float subTotal;
    private Long productId;
}
