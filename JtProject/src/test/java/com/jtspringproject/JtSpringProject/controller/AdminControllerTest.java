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
}
