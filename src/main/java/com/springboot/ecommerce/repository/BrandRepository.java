package com.springboot.ecommerce.repository;

import com.springboot.ecommerce.entity.Brand;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BrandRepository extends JpaRepository<Brand, Long> {
    List<Brand> findByCategoryId(Long categoryId);
}
