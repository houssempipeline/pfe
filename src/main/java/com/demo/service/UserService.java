package com.demo.service;

import com.demo.model.User;
import com.demo.repository.UserRepository;
import com.demo.dto.RegistrationDto;
import com.demo.exception.UserRegistrationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class UserService implements UserDetailsService {

    private static final String USER_NOT_FOUND = "User not found";
    private static final String USERNAME_EXISTS = "Username already exists";
    private static final String EMAIL_EXISTS = "Email already exists";

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User registerNewUser(RegistrationDto registrationDto) {
        if (userRepository.existsByUsername(registrationDto.getUsername())) {
            throw new UserRegistrationException(USERNAME_EXISTS);
        }
        if (userRepository.existsByEmail(registrationDto.getEmail())) {
            throw new UserRegistrationException(EMAIL_EXISTS);
        }

        User user = new User();
        user.setUsername(registrationDto.getUsername());
        user.setEmail(registrationDto.getEmail());
        user.setPassword(passwordEncoder.encode(registrationDto.getPassword()));
        user.setInitialBalance(registrationDto.getInitialBalance());

        return userRepository.save(user);
    }

    public User getUserByUsername(String username) {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new RuntimeException(USER_NOT_FOUND); // can also extract a new UserNotFoundException if needed
        }
        return user;
    }

    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new RuntimeException(USER_NOT_FOUND); // same here
        }
        return user;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException(USER_NOT_FOUND);
        }

        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                Collections.singletonList(new SimpleGrantedAuthority("USER"))
        );
    }
}
