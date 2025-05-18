package com.demo;

import com.demo.controller.WebController;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;

@Import(TestSecurityConfig.class)
@WebMvcTest(WebController.class)
public class WebControllerTest {

    @Test
    void contextLoads() {
        // Context loads for WebController slice test
    }
}
