package com.demo.config;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class SecurityConfigTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("Public route /login should be accessible without authentication")
    void testLoginPageIsAccessible() throws Exception {
        mockMvc.perform(get("/login"))
               .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Protected route /dashboard should redirect to login if unauthenticated")
    void testDashboardRequiresAuthentication() throws Exception {
        mockMvc.perform(get("/dashboard"))
               .andExpect(status().is3xxRedirection())
               .andExpect(redirectedUrlPattern("**/login"));
    }

    @Test
    @DisplayName("Protected route /dashboard should be accessible with authentication")
    @WithMockUser(username = "testuser", roles = {"USER"})
    void testDashboardIsAccessibleWithAuthentication() throws Exception {
        mockMvc.perform(get("/dashboard"))
               .andExpect(status().isOk());
    }
}
