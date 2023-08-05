package com.springboot.ecommerce.Resume.repository;

import com.springboot.ecommerce.Resume.entity.CV.CV;
import org.springframework.data.jpa.repository.JpaRepository;


public interface CVRepository extends JpaRepository<CV, Long> {

}
