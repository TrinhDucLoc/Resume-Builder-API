package com.springboot.ecommerce.repository.address;

import com.springboot.ecommerce.entity.address.District;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DistrictRepository extends JpaRepository<District, Long> {
    List<District> findByProvinceId(Long provinceId);
    District findByFullName(String districtName);
}
