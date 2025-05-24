package com.demo;

import com.demo.controller.UserController;
import com.demo.dto.RegistrationDto;
import com.demo.model.User;
import com.demo.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class UserControllerTest {

    @Mock private UserService userService;
    @Mock private Model model;
    @Mock private BindingResult bindingResult;

    @InjectMocks private UserController userController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testShowRegistrationForm() {
        String view = userController.showRegistrationForm(model);
        verify(model).addAttribute(eq("user"), any(RegistrationDto.class));
        assertEquals("register", view);
    }

    @Test
    void testRegisterUserSuccess() {
        RegistrationDto dto = new RegistrationDto();
        when(bindingResult.hasErrors()).thenReturn(false);
        when(userService.registerNewUser(any())).thenReturn(new User("alice", "alice@example.com", "pwd", 100.0));

        String view = userController.registerUser(dto, bindingResult, model);

        assertEquals("redirect:/login?success", view);
        verify(userService).registerNewUser(any());
    }

    @Test
    void testRegisterUserValidationError() {
        RegistrationDto dto = new RegistrationDto();
        when(bindingResult.hasErrors()).thenReturn(true);

        String view = userController.registerUser(dto, bindingResult, model);

        assertEquals("register", view);
        verify(userService, never()).registerNewUser(any());
    }

    @ParameterizedTest
    @CsvSource({
        "Username already exists,username,Username already exists",
        "Email already exists,email,Email already exists",
        "Unexpected error,,Registration error"
    })
    void testRegisterUserExceptions(String exceptionMessage, String field, String expectedMessage) {
        RegistrationDto dto = new RegistrationDto();
        when(bindingResult.hasErrors()).thenReturn(false);
        doThrow(new RuntimeException(exceptionMessage)).when(userService).registerNewUser(any());

        String view = userController.registerUser(dto, bindingResult, model);

        verify(bindingResult).rejectValue(field, "error.user", expectedMessage);
        assertEquals("register", view);
    }

    @Test
    void testShowLoginForm() {
        String view = userController.showLoginForm();
        assertEquals("login", view);
    }

    @Test
    void testShowDashboard() {
        User mockUser = new User("john", "john@example.com", "pass", 200.0);
        when(userService.getCurrentUser()).thenReturn(mockUser);

        String view = userController.showDashboard(model);

        verify(model).addAttribute("user", mockUser);
        assertEquals("dashboard", view);
    }
}
