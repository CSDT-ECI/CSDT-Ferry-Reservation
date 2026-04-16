package com.jtspringproject.JtSpringProject.controller;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.servlet.ModelAndView;

import com.jtspringproject.JtSpringProject.models.Product;
import com.jtspringproject.JtSpringProject.services.categoryService;
import com.jtspringproject.JtSpringProject.services.productService;
import com.jtspringproject.JtSpringProject.services.userService;
import org.springframework.ui.Model;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import com.jtspringproject.JtSpringProject.models.Category;
import com.jtspringproject.JtSpringProject.models.Product;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.*;

@ExtendWith(MockitoExtension.class)
class AdminControllerTest {

    @Mock
    private userService userService;

    @Mock
    private categoryService categoryService;

    @Mock
    private productService productService;

    @Test
    void getproductAddsMessageWhenProductListIsEmpty() {
        AdminController adminController = new AdminController(userService, categoryService, productService);
        when(productService.getProducts()).thenReturn(Collections.emptyList());

        ModelAndView result = adminController.getproduct();

        assertEquals("products", result.getViewName());
        assertEquals("No products are available", result.getModel().get("msg"));
        assertNull(result.getModel().get("products"));
        verify(productService).getProducts();
    }

    @Test
    void getproductAddsProductsWhenDataExists() {
        AdminController adminController = new AdminController(userService, categoryService, productService);
        Product product = new Product();
        product.setName("Ferry Item");
        List<Product> products = Collections.singletonList(product);
        when(productService.getProducts()).thenReturn(products);

        ModelAndView result = adminController.getproduct();

        assertEquals("products", result.getViewName());
        assertEquals(products, result.getModel().get("products"));
        assertNull(result.getModel().get("msg"));
        verify(productService).getProducts();
    }

    @Test
    void updateProductPostReturnsRedirectToProducts() {
        AdminController adminController = new AdminController(userService, categoryService, productService);
        String view = adminController.updateProduct(10, "name", 1, 100, 2, 3, "desc", "img.png");

        assertEquals("redirect:/admin/products", view);
    }
    @Test
void indexAddsUsernameToModel() {
    AdminController controller = new AdminController(userService, categoryService, productService);

    Model model = mock(Model.class);

    SecurityContextHolder.getContext().setAuthentication(
            new UsernamePasswordAuthenticationToken("adminUser", null)
    );

    String view = controller.index(model);

    assertEquals("index", view);
    verify(model).addAttribute("username", "adminUser");
}   
@Test
void adminLoginShowsErrorMessage() {
    AdminController controller = new AdminController(userService, categoryService, productService);

    ModelAndView mv = controller.adminlogin("true");

    assertEquals("adminlogin", mv.getViewName());
    assertEquals("Invalid username or password. Please try again.", mv.getModel().get("msg"));
}@Test
void getCategoriesReturnsList() {
    AdminController controller = new AdminController(userService, categoryService, productService);

    List<Category> list = List.of(new Category());
    when(categoryService.getCategories()).thenReturn(list);

    ModelAndView mv = controller.getcategory();

    assertEquals("categories", mv.getViewName());
    assertEquals(list, mv.getModel().get("categories"));
}
@Test
void addProductShouldCallService() {
    AdminController controller = new AdminController(userService, categoryService, productService);

    Category category = new Category();
    when(categoryService.getCategory(1)).thenReturn(category);

    String view = controller.addProduct(
            "Test", 1, 100, 2, 3, "desc", "img"
    );

    verify(productService).addProduct(any(Product.class));
    assertEquals("redirect:/admin/products", view);
}
}
