package com.demo.dto;

import jakarta.validation.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class RegistrationDtoTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void validDtoShouldHaveNoViolations() {
        RegistrationDto dto = new RegistrationDto();
        dto.setUsername("validUser");
        dto.setEmail("user@example.com");
        dto.setPassword("securePass");
        dto.setInitialBalance(100.0);

        Set<ConstraintViolation<RegistrationDto>> violations = validator.validate(dto);
        assertTrue(violations.isEmpty());
    }

    @Test
    void shortPasswordShouldTriggerViolation() {
        RegistrationDto dto = new RegistrationDto();
        dto.setUsername("user");
        dto.setEmail("user@example.com");
        dto.setPassword("123");
        dto.setInitialBalance(50.0);

        Set<ConstraintViolation<RegistrationDto>> violations = validator.validate(dto);
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("password")));
    }

    @Test
    void invalidEmailShouldTriggerViolation() {
        RegistrationDto dto = new RegistrationDto();
        dto.setUsername("user");
        dto.setEmail("invalid-email");
        dto.setPassword("securePass");
        dto.setInitialBalance(50.0);

        Set<ConstraintViolation<RegistrationDto>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
    }

    @Test
    void negativeInitialBalanceShouldTriggerViolation() {
        RegistrationDto dto = new RegistrationDto();
        dto.setUsername("user");
        dto.setEmail("user@example.com");
        dto.setPassword("securePass");
        dto.setInitialBalance(-10.0);

        Set<ConstraintViolation<RegistrationDto>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
    }

    @Test
    void nullInitialBalanceShouldTriggerViolation() {
        RegistrationDto dto = new RegistrationDto();
        dto.setUsername("user");
        dto.setEmail("user@example.com");
        dto.setPassword("securePass");
        dto.setInitialBalance(null);

        Set<ConstraintViolation<RegistrationDto>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
    }
}
