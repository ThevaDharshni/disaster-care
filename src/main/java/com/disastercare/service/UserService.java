package com.disastercare.service;

import com.disastercare.model.User;
import com.disastercare.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    @Autowired private UserRepository userRepository;
    @Autowired private PasswordEncoder passwordEncoder;

    public User register(String name, String rawPassword) {
        User user = new User();
        user.setName(name);
        user.setPassword(passwordEncoder.encode(rawPassword));
        user.setUserId(generateUserId());
        return userRepository.save(user);
    }

    public Optional<User> login(String name, String rawPassword) {
        Optional<User> userOpt = userRepository.findByName(name);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            if (passwordEncoder.matches(rawPassword, user.getPassword())) {
                return Optional.of(user);
            }
        }
        return Optional.empty();
    }

    private String generateUserId() {
        long count = userRepository.count() + 1;
        return String.format("USR%05d", count);
    }

    public long getTotalUsers() { return userRepository.count(); }
}
