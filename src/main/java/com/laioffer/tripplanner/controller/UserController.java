package com.laioffer.tripplanner.controller;

import com.laioffer.tripplanner.entity.UserEntity;
import com.laioffer.tripplanner.model.*;
import com.laioffer.tripplanner.repository.UserRepository;
import com.laioffer.tripplanner.service.UserService;
import com.laioffer.tripplanner.util.JWTUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.xml.crypto.Data;

@RequestMapping("/auth")
@RestController
public class UserController {

    private final UserService userService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JWTUtil jwtUtil;

    public UserController(UserService userService,  UserRepository userRepository,  PasswordEncoder passwordEncoder,  JWTUtil jwtUtil) {
        this.userService = userService;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/signup")
    public ResponseEntity<?> register(@RequestBody RegisterBody request) {
        String err = userService.register(request.email(), request.password(), request.username());
        UserData userData = new UserData(request.password(), request.email(), request.username());
        if(err != null) {
            RegisterResponse r = new RegisterResponse(false, userData, err);
            return ResponseEntity.badRequest().body(r);
        }
        RegisterResponse r = new RegisterResponse(true, userData, null);
        return ResponseEntity.ok(r);
    }
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginBody request) {

        UserEntity user = userRepository.findByEmail(request.email());
        if (user == null) {
            throw  new UsernameNotFoundException("email not found");
        }


        if (!passwordEncoder.matches(request.password(), user.getPassword())) {
            throw new RuntimeException("Invalid password");
        }

        String token = jwtUtil.generateToken(user.getEmail());
        UserData userData = new UserData(user.getPassword(), user.getEmail(), user.getUsername());
        LoginResponse r = new LoginResponse(true, userData, token, null);


        return ResponseEntity.ok(r);
    }
}
