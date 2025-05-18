package com.demo;

import com.demo.controller.UserController;
import com.demo.dto.RegistrationDto;
import com.demo.model.User;
import com.demo.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class UserControllerTest {

    @Mock
    private UserService userService;

    @Mock
    private Model model;

    @Mock
    private BindingResult bindingResult;

    @InjectMocks
    private UserController userController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRegisterUserSuccess() {
        RegistrationDto dto = new RegistrationDto();
        when(bindingResult.hasErrors()).thenReturn(false);
        when(userService.registerNewUser(any(RegistrationDto.class)))
            .thenReturn(new User("alice", "alice@example.com", "pwd", 100.0));

        String view = userController.registerUser(dto, bindingResult, model);

        assertEquals("redirect:/login?success", view);
        verify(userService).registerNewUser(any(RegistrationDto.class));
    }

    @Test
    void testRegisterUserValidationError() {
        RegistrationDto dto = new RegistrationDto();
        when(bindingResult.hasErrors()).thenReturn(true);

        String view = userController.registerUser(dto, bindingResult, model);

        assertEquals("register", view);
        verify(userService, never()).registerNewUser(any());
    }
}
