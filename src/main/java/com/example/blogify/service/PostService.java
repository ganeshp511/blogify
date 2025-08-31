package com.example.blogify.service;

import com.example.blogify.dto.PostRequest;
import com.example.blogify.dto.PostResponse;
import com.example.blogify.entity.Post;
import com.example.blogify.entity.User;
import com.example.blogify.repository.PostRepository;
import com.example.blogify.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostService {
    private final UserRepository userRepository;
    private final PostRepository postRepository;
    public PostResponse createPost(PostRequest postRequest, String username) {
        User author=userRepository.findByUserName(username).orElseThrow(()->new UsernameNotFoundException("User not found"));

        Post post=Post.builder().title(postRequest.getTitle()).content(postRequest.getContent()).authorId(author).build();
        Post savedPost=postRepository.save(post);
        return mapToPostResponse(savedPost);

    }

    private PostResponse mapToPostResponse(Post post){
        return PostResponse.builder().id(post.getPostId()).title(post.getTitle()).content(post.getTitle()).author(post.getAuthorId().getUserName()).createdAt(post.getCreatedAt()).updatedAt(post.getUpdatedAt()).build();
    }
}
