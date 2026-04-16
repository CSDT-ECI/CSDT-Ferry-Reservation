package com.jtspringproject.JtSpringProject.services;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.*;
import com.jtspringproject.JtSpringProject.dao.userDao;
import com.jtspringproject.JtSpringProject.models.User;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private userDao userDao;

    @InjectMocks
    private userService userService;

    @Test
    void getUsersReturnsDaoResult() {
        User first = new User();
        first.setUsername("alice");
        User second = new User();
        second.setUsername("bob");
        List<User> expected = Arrays.asList(first, second);

        when(userDao.getAllUser()).thenReturn(expected);

        List<User> result = userService.getUsers();

        assertEquals(2, result.size());
        assertEquals("alice", result.get(0).getUsername());
        assertEquals("bob", result.get(1).getUsername());
        verify(userDao).getAllUser();
    }

    @Test
    void addUserThrowsRuntimeExceptionWhenConstraintFails() {
        User user = new User();
        user.setUsername("taken-user");

        when(userDao.saveUser(user)).thenThrow(new DataIntegrityViolationException("duplicate username"));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> userService.addUser(user));

        assertEquals("Add user error", exception.getMessage());
        verify(userDao).saveUser(user);
    }

    @Test
    void checkUserExistsDelegatesToDao() {
        when(userDao.userExists("john")).thenReturn(true);

        boolean exists = userService.checkUserExists("john");

        assertEquals(true, exists);
        verify(userDao).userExists("john");
    }

    @Test
    void checkLoginReturnsDaoUser() {
        User user = new User();
        user.setUsername("john");

        when(userDao.getUser("john", "123")).thenReturn(user);

        User result = userService.checkLogin("john", "123");

        assertNotNull(result);
        assertEquals("john", result.getUsername());
        verify(userDao).getUser("john", "123");
    }
    @Test
void getUserByUsernameDelegatesToDao() {
    User user = new User();
    user.setUsername("julian");

    when(userDao.getUserByUsername("julian")).thenReturn(user);

    User result = userService.getUserByUsername("julian");

    assertNotNull(result);
    assertEquals("julian", result.getUsername());
    verify(userDao).getUserByUsername("julian");
}   
@Test
void addUserSuccess() {
    User user = new User();
    user.setUsername("new-user");

    when(userDao.saveUser(user)).thenReturn(user);

    User result = userService.addUser(user);

    assertNotNull(result);
    assertEquals("new-user", result.getUsername());
    verify(userDao).saveUser(user);
}
}
