package com.springboot.ecommerce.repository.address;

import com.springboot.ecommerce.entity.address.Province;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProvinceRepository extends JpaRepository<Province, Long> {
    Province findByFullName(String provinceName);

}
