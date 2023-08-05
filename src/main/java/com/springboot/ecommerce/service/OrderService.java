package com.springboot.ecommerce.service;

import com.springboot.ecommerce.dto.OrderResponse;
import com.springboot.ecommerce.dto.ProductResponse;
import com.springboot.ecommerce.entity.User;
import com.springboot.ecommerce.payload.OrderDetailRequest;
import com.springboot.ecommerce.payload.OrderRequest;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;
import java.util.List;

public interface OrderService {
    OrderResponse createOrder(OrderRequest orderRequest, Long userId) throws MessagingException, UnsupportedEncodingException;

    List<OrderResponse> getAllOrder();

//    OrderResponse getOrderById(Long orderId);
    OrderRequest getOrderById(Long orderId);

    List<OrderResponse> getOrderByUserId(Long userId);

//    List<OrderResponse> getOrderByAccessToken(String accessToken);


//    Admin - Comfirm order
    OrderResponse comfirmOrderById(Long id);
//    Admin - Cancel order
    OrderResponse cancelOrderById(Long id);
//    Shipper - Delivering order
    OrderResponse deliveringOrderById(Long id);
//    Shipper - Done order product
    OrderResponse doneOrderById(Long id) throws MessagingException, UnsupportedEncodingException;

//    void sendVerificationEmail(User user) throws MessagingException, UnsupportedEncodingException;
}
