package com.springboot.ecommerce.service.impl;

import com.springboot.ecommerce.entity.Comment;
import com.springboot.ecommerce.entity.Post;
import com.springboot.ecommerce.exception.ResourceNotFoundException;
import com.springboot.ecommerce.payload.CommentDto;
import com.springboot.ecommerce.payload.PostDto;
import com.springboot.ecommerce.dto.PostResponse;
import com.springboot.ecommerce.repository.CommentRepository;
import com.springboot.ecommerce.repository.PostRepository;
import com.springboot.ecommerce.service.PostService;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;

    private final CommentRepository commentRepository;

    private final ModelMapper mapper;

    public PostServiceImpl(PostRepository postRepository, CommentRepository commentRepository, ModelMapper mapper) {
        this.postRepository = postRepository;
        this.commentRepository = commentRepository;
        this.mapper = mapper;
    }
//    public PostServiceImpl(PostRepository postRepository, ModelMapper mapper) {
//          this.postRepository = postRepository;
//          this.mapper = mapper;
//    }

    @Override
    public PostDto createPost(PostDto postDto, CommentDto commentDto) {

        // convert DTO to entity
        Post post = mapToEntity(postDto);
//
        Set<CommentDto> commentDtoSet = new HashSet<>();
//
        commentDtoSet = postDto.getComments();
//
        Set<Comment> comments = new HashSet<>();

//        For each commentDto form List commentDtos
        for (CommentDto cm: commentDtoSet) {
//            Create comment entity object form DTO
            Comment comment  = mapper.map(cm, Comment.class);
//            Save post id
            comment.setPost(post);
//            Add each comment into list comments
            comments.add(comment);
        }
//        post save list comments
        post.setComments(comments);


        Post newPost = postRepository.save(post);


        Long postId = newPost.getId();
//
//        Post post2 = postRepository.findById(postId).orElseThrow(
//                () -> new ResourceNotFoundException("Post", "id", postId));
//
////        Comment set
//        Comment comment = new Comment();
//
//        // set post to comment entity
//        comment.setPost(post2);
//
//        // comment entity to DB
//        Comment newComment =  commentRepository.save(comment);
//
//        mapper.map(newComment, CommentDto.class);
//
////        CommentDto commentDto1 = mapToDTO(newComment);

        // convert entity to DTO
        PostDto postResponse = mapToDTO(newPost);
        return postResponse;
    }

    @Override
    public PostResponse getAllPosts(int pageNo, int pageSize, String sortBy, String sortDir) {

        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        // create Pageable instance
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);

        Page<Post> posts = postRepository.findAll(pageable);

        // get content for page object
        List<Post> listOfPosts = posts.getContent();

        List<PostDto> content= listOfPosts.stream().map(post -> mapToDTO(post)).collect(Collectors.toList());

        PostResponse postResponse = new PostResponse();
        postResponse.setContent(content);
        postResponse.setPageNo(posts.getNumber());
        postResponse.setPageSize(posts.getSize());
        postResponse.setTotalElements(posts.getTotalElements());
        postResponse.setTotalPages(posts.getTotalPages());
        postResponse.setLast(posts.isLast());

        return postResponse;
    }

    @Override
    public PostDto getPostById(long id) {
        Post post = postRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Post", "id", id));
        return mapToDTO(post);
    }

    @Override
    public PostDto updatePost(PostDto postDto, long id) {
        // get post by id from the database
        Post post = postRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Post", "id", id));

        post.setTitle(postDto.getTitle());
        post.setDescription(postDto.getDescription());
        post.setContent(postDto.getContent());

        Post updatedPost = postRepository.save(post);
        return mapToDTO(updatedPost);
    }

    @Override
    public void deletePostById(long id) {
        // get post by id from the database
        Post post = postRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Post", "id", id));
        postRepository.delete(post);
    }

    // convert Entity into DTO
    private PostDto mapToDTO(Post post){
        PostDto postDto = mapper.map(post, PostDto.class);
//        PostDto postDto = new PostDto();
//        postDto.setId(post.getId());
//        postDto.setTitle(post.getTitle());
//        postDto.setDescription(post.getDescription());
//        postDto.setContent(post.getContent());
        return postDto;
    }

    // convert DTO to entity
    private Post mapToEntity(PostDto postDto){
        Post post = mapper.map(postDto, Post.class);
//        Post post = new Post();
//        post.setTitle(postDto.getTitle());
//        post.setDescription(postDto.getDescription());
//        post.setContent(postDto.getContent());
        return post;
    }
}
