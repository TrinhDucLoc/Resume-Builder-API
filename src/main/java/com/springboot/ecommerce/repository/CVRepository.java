package com.springboot.ecommerce.repository;

import com.springboot.ecommerce.entity.CV.CV;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface CVRepository extends JpaRepository<CV, Long> {
    List<CV> findByUserId(Long userId);
}
