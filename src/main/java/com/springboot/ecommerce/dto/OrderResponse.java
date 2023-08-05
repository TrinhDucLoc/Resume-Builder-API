package com.springboot.ecommerce.dto;

import com.springboot.ecommerce.payload.OrderDetailRequest;
import com.springboot.ecommerce.payload.OrderRequest;
import com.springboot.ecommerce.payload.PostDto;
import io.swagger.annotations.Api;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Api(value = "Order model information")
@Data
public class OrderResponse {
    private Long id;
    private String address;
    private String phoneNumber;
    private Float productCost;
    private Float shippingCost;
    private Float totalCost;
    private String paymentMethod;
    private Date orderTime;
    private String orderStatus;
//    private List<OrderRequest> orderRequests;
}
