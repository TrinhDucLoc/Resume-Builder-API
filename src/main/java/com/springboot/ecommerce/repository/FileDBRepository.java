package com.springboot.ecommerce.repository;

import com.springboot.ecommerce.entity.FileDB;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FileDBRepository extends JpaRepository<FileDB, String> {
//    void delete(Optional<FileDB> fileDB);
}
