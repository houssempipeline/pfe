package com.demo;

import com.demo.controller.WebController;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.beans.factory.annotation.Autowired;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(WebController.class)
@Import(TestSecurityConfig.class)
class WebControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("GET /main should redirect to /dashboard")
    void testMainPageRedirectsToDashboard() throws Exception {
        mockMvc.perform(get("/main"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/dashboard"));
    }

    @Test
    @DisplayName("GET /service1 should return service1 view")
    void testService1View() throws Exception {
        mockMvc.perform(get("/service1"))
                .andExpect(status().isOk())
                .andExpect(view().name("service1"));
    }

    @Test
    @DisplayName("GET /service2 should return service2 view")
    void testService2View() throws Exception {
        mockMvc.perform(get("/service2"))
                .andExpect(status().isOk())
                .andExpect(view().name("service2"));
    }

    @Test
    @DisplayName("GET /contact should return contact view")
    void testContactView() throws Exception {
        mockMvc.perform(get("/contact"))
                .andExpect(status().isOk())
                .andExpect(view().name("contact"));
    }
}
