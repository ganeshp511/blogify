package com.example.blogify.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    private String userName;

    @Column(nullable = false, unique = true)
    @Email
    private String email;

    @Size(min = 8, message = "Password should be atleast 8 characters")
    private String password;

    @CreationTimestamp
    private String createdAt;

    @OneToMany(mappedBy = "authorId", cascade = CascadeType.ALL, orphanRemoval = false)
    private List<Post> posts=new ArrayList<>();
}