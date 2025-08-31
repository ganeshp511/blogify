package com.example.blogify.controller;

import com.example.blogify.dto.PostRequest;
import com.example.blogify.dto.PostResponse;
import com.example.blogify.service.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.net.http.HttpResponse;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/posts")
public class PostController {
    private final PostService postService;

    @PreAuthorize("isAuthenticated()")
    @PostMapping
    public ResponseEntity<PostResponse> createPosts(@Valid @RequestBody PostRequest postRequest, @AuthenticationPrincipal UserDetails userDetails){
        return ResponseEntity.status(HttpStatus.CREATED).body(postService.createPost(postRequest,userDetails.getUsername()));

    }
    @GetMapping
    public ResponseEntity<List<PostResponse>>getAlllPosts(@RequestParam(defaultValue="0")int page,
    @RequestParam(defaultValue = "10")int size,@RequestParam(defaultValue = "createdAt")String sortBy){
        return ResponseEntity.ok(postService.getAllPosts(page,size,sortBy));
    }

}
