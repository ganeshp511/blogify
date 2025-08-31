package com.example.blogify.service;

import com.example.blogify.dto.AuthResponse;
import com.example.blogify.dto.LoginRequest;
import com.example.blogify.dto.RegisterRequest;
import com.example.blogify.entity.User;
import com.example.blogify.repository.UserRepository;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.example.blogify.config.JWTUtil;

import javax.security.sasl.AuthenticationException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JWTUtil jwtService;

    public AuthResponse registerUser(RegisterRequest request) {
        // Logic to register user
        User user=User.builder()
                .userName(request.getUserName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .build();
        userRepository.save(user);

        return AuthResponse.builder().username(user.getUserName()).build();
    }

    public AuthResponse loginUser(LoginRequest request) throws AuthenticationException {
        //Authenticate user
        Optional<User> userOpt = userRepository.findByUserName(request.getUserName());
        User user = userOpt.orElseThrow(() -> new AuthenticationException("Invalid username"));
        if(!passwordEncoder.matches(request.getPassword(),user.getPassword())){
            throw new AuthenticationException("Invalid password");
        }
        //Generate JWT token
        String token=jwtService.generateToken(user.getUserName());
        //Return AuthResponse
        return AuthResponse.builder().token(token).username(user.getUserName()).build();
    }
}
