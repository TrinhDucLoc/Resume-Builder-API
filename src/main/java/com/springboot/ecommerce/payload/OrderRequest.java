package com.springboot.ecommerce.payload;

import com.springboot.ecommerce.entity.OrderDetail;
import com.springboot.ecommerce.entity.ShippingAddress;
import io.swagger.annotations.Api;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Api(value = "Order model information")
@Data
public class OrderRequest {
    private String address;
    private String phoneNumber;
    private Float productCost;
    private Float shippingCost;
    private String paymentMethod;
//    private String orderStatus;
//    private String qrCode;
    private Float totalCost;
    private Set<OrderDetailRequest> orderDetails;

//    private Set<ShippingAddressRequest> shippingAddress;
}
