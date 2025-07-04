package com.demo;

import com.demo.dto.RegistrationDto;
import com.demo.model.User;
import com.demo.repository.UserRepository;
import com.demo.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRegisterNewUserSuccess() {
        RegistrationDto dto = new RegistrationDto();
        dto.setUsername("alice");
        dto.setEmail("alice@example.com");
        dto.setPassword("password");
        dto.setInitialBalance(100.0);

        when(userRepository.existsByUsername("alice")).thenReturn(false);
        when(userRepository.existsByEmail("alice@example.com")).thenReturn(false);
        when(passwordEncoder.encode("password")).thenReturn("encoded");
        User saved = new User("alice","alice@example.com","encoded",100.0);
        when(userRepository.save(any(User.class))).thenReturn(saved);

        User result = userService.registerNewUser(dto);
        assertNotNull(result);
        assertEquals("alice", result.getUsername());
        assertEquals("alice@example.com", result.getEmail());
        assertEquals("encoded", result.getPassword());
        assertEquals(100.0, result.getInitialBalance());
        verify(userRepository).save(any(User.class));
    }

    @Test
    void testRegisterNewUserUsernameExists() {
        RegistrationDto dto = new RegistrationDto();
        dto.setUsername("bob");
        dto.setEmail("bob@example.com");
        dto.setPassword("pass");
        dto.setInitialBalance(50.0);

        when(userRepository.existsByUsername("bob")).thenReturn(true);

        assertThrows(RuntimeException.class, () -> userService.registerNewUser(dto));
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void testRegisterNewUserEmailExists() {
        RegistrationDto dto = new RegistrationDto();
        dto.setUsername("charlie");
        dto.setEmail("charlie@example.com");
        dto.setPassword("pass");
        dto.setInitialBalance(75.0);

        when(userRepository.existsByUsername("charlie")).thenReturn(false);
        when(userRepository.existsByEmail("charlie@example.com")).thenReturn(true);

        assertThrows(RuntimeException.class, () -> userService.registerNewUser(dto));
        verify(userRepository, never()).save(any(User.class));
    }
}
