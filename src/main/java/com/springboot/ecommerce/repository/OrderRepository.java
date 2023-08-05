package com.springboot.ecommerce.repository;

import com.springboot.ecommerce.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUserId(Long userId);

//    List<Order> findByAccessToken(String accessToken);
}
