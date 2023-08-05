package com.springboot.ecommerce.service;

import com.springboot.ecommerce.payload.CommentDto;
import com.springboot.ecommerce.payload.PostDto;
import com.springboot.ecommerce.dto.PostResponse;

public interface PostService {
    PostDto createPost(PostDto postDto, CommentDto commentDto);

    PostResponse getAllPosts(int pageNo, int pageSize, String sortBy, String sortDir);

    PostDto getPostById(long id);

    PostDto updatePost(PostDto postDto, long id);

    void deletePostById(long id);
}
