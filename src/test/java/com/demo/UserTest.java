package com.demo;

import com.demo.model.User;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class UserTest {

    @Test
    public void testUserConstructor() {
        User u = new User("alice", "alice@example.com", "pwd", 100.0);
        assertEquals("alice", u.getUsername());
        assertEquals("alice@example.com", u.getEmail());
        assertEquals("pwd", u.getPassword());
        assertEquals(100.0, u.getInitialBalance());
    }

    @Test
    public void testUserGettersAndSetters() {
        User u = new User();
        u.setUsername("bob");
        u.setEmail("bob@example.com");
        u.setPassword("1234");
        u.setInitialBalance(50.0);
        assertEquals("bob", u.getUsername());
        assertEquals("bob@example.com", u.getEmail());
        assertEquals("1234", u.getPassword());
        assertEquals(50.0, u.getInitialBalance());
    }

    @Test
    public void testUserEqualsAndHashCode() {
        User a = new User("x", "x@x.com", "p", 1.0);
        User b = new User("x", "x@x.com", "p", 1.0);
        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());
    }
}
