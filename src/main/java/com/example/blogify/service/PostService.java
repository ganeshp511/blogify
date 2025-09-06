package com.example.blogify.service;

import com.example.blogify.dto.PostRequest;
import com.example.blogify.dto.PostResponse;
import com.example.blogify.entity.Post;
import com.example.blogify.entity.User;
import com.example.blogify.repository.PostRepository;
import com.example.blogify.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {
    private final UserRepository userRepository;
    private final PostRepository postRepository;

    public PostResponse createPost(PostRequest postRequest, String email) {
        User author = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        Post post = Post.builder()
                .title(postRequest.getTitle())
                .content(postRequest.getContent())
                .authorId(author)
                .build();

        Post savedPost = postRepository.save(post);
        return mapToPostResponse(savedPost);
    }

    public List<PostResponse> getAllPosts(int page, int size, String sortBy) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy).descending());
        return postRepository.findAll(pageable).map(this::mapToPostResponse).getContent();
    }

    public PostResponse getPostById(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found with id: " + postId));
        return mapToPostResponse(post);
    }

    public void deletePost(Long postId, String email) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));

        // Use email instead of userName for consistency
        if (!post.getAuthorId().getEmail().equals(email)) {
            throw new RuntimeException("You can only delete your own posts");
        }
        postRepository.delete(post);
    }

    public PostResponse updatePost(Long postId, PostRequest postRequest, String email) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));

        // Use email instead of userName for consistency
        if (!post.getAuthorId().getEmail().equals(email)) {
            throw new RuntimeException("You can only update your own posts");
        }

        post.setTitle(postRequest.getTitle());
        post.setContent(postRequest.getContent());
        Post updatedPost = postRepository.save(post);
        return mapToPostResponse(updatedPost);
    }

    private PostResponse mapToPostResponse(Post post) {
        return PostResponse.builder()
                .id(post.getPostId())
                .title(post.getTitle())
                .content(post.getContent()) // Fixed: was showing title instead of content
                .author(post.getAuthorId().getUserName())
                .createdAt(post.getCreatedAt())
                .updatedAt(post.getUpdatedAt())
                .build();
    }
}