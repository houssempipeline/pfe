package com.demo.config;

import com.demo.model.User;
import com.demo.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class SecurityConfigTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserRepository userRepository;

    @Test
    @DisplayName("Protected route /user/dashboard should be accessible with authentication")
    void testDashboardIsAccessibleWithAuthentication() throws Exception {
        // Arrange: mock a domain User entity
        User mockUser = new User();
        mockUser.setUsername("testuser");
        mockUser.setPassword("encodedpass");

        when(userRepository.findByUsername("testuser")).thenReturn(mockUser);

        // Inject real domain User object into Spring Security context
        SecurityContextHolder.getContext().setAuthentication(
            new UsernamePasswordAuthenticationToken(mockUser, null, List.of())
        );

        // Act + Assert
        mockMvc.perform(get("/user/dashboard"))
               .andExpect(status().isOk())
               .andExpect(view().name("dashboard"))
               .andExpect(model().attributeExists("user"));
    }
}
