package com.springboot.ecommerce.Resume.repository;

import com.springboot.ecommerce.Resume.entity.CV.CV;
import com.springboot.ecommerce.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface CVRepository extends JpaRepository<CV, Long> {
    List<CV> findByUserId(Long userId);
}
