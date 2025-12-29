package com.laioffer.tripplanner.service;

import com.laioffer.tripplanner.entity.UserEntity;
import com.laioffer.tripplanner.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public String register(String email, String password, String username) {
        email = email.toLowerCase();

        if (userRepository.existsByEmail(email)) {
            return "Username already exists";
        }

        UserEntity user = new UserEntity();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        user.setEmail(email);

        userRepository.save(user);
        return null;
    }
}