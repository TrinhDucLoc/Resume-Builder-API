package com.springboot.ecommerce.repository;

import com.springboot.ecommerce.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {

}
