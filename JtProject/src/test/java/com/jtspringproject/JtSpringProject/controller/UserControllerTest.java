package com.jtspringproject.JtSpringProject.controller;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.servlet.ModelAndView;

import com.jtspringproject.JtSpringProject.models.Product;
import com.jtspringproject.JtSpringProject.models.User;
import com.jtspringproject.JtSpringProject.services.productService;
import com.jtspringproject.JtSpringProject.services.userService;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @Mock
    private userService userService;

    @Mock
    private productService productService;

    @Test
    void newUseRegisterCreatesUserWhenUsernameIsAvailable() {
        UserController userController = new UserController(userService, productService);
        User user = new User();
        user.setUsername("new-user");
        user.setEmail("new@user.com");

        when(userService.checkUserExists("new-user")).thenReturn(false);

        ModelAndView result = userController.newUseRegister(user);

        assertEquals("userLogin", result.getViewName());
        assertEquals("ROLE_NORMAL", user.getRole());
        verify(userService).checkUserExists("new-user");
        verify(userService).addUser(user);
        assertNull(result.getModel().get("msg"));
    }

    @Test
    void newUseRegisterReturnsRegisterWhenUsernameAlreadyExists() {
        UserController userController = new UserController(userService, productService);
        User user = new User();
        user.setUsername("existing-user");

        when(userService.checkUserExists("existing-user")).thenReturn(true);

        ModelAndView result = userController.newUseRegister(user);

        assertEquals("register", result.getViewName());
        assertNotNull(result.getModel().get("msg"));
        assertEquals("existing-user is taken. Please choose a different username.", result.getModel().get("msg"));
        verify(userService).checkUserExists("existing-user");
        verify(userService, never()).addUser(user);
    }

    @Test
    void indexPageShowsMessageWhenNoProductsAreAvailable() {
        UserController userController = new UserController(userService, productService);
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken("tester", "pwd")
        );
        when(productService.getProducts()).thenReturn(Collections.emptyList());

        ModelAndView result = userController.indexPage();

        assertEquals("index", result.getViewName());
        assertEquals("tester", result.getModel().get("username"));
        assertEquals("No products are available", result.getModel().get("msg"));
        verify(productService).getProducts();
        SecurityContextHolder.clearContext();
    }

    @Test
    void indexPageShowsProductsWhenDataExists() {
        UserController userController = new UserController(userService, productService);
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken("tester", "pwd")
        );
        Product product = new Product();
        product.setName("Ferry Ticket");
        List<Product> products = Collections.singletonList(product);
        when(productService.getProducts()).thenReturn(products);

        ModelAndView result = userController.indexPage();

        assertEquals("index", result.getViewName());
        assertEquals("tester", result.getModel().get("username"));
        assertEquals(products, result.getModel().get("products"));
        assertNull(result.getModel().get("msg"));
        verify(productService).getProducts();
        SecurityContextHolder.clearContext();
    }
}
