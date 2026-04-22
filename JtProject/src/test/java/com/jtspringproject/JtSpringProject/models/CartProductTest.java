package com.jtspringproject.JtSpringProject.models;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.Test;

class CartProductTest {

    @Test
    void testConstructorAndGetters() {
        Cart cart = new Cart();
        cart.setId(1);

        Product product = new Product();
        product.setId(100);

        CartProduct cartProduct = new CartProduct(cart, product);

        assertNotNull(cartProduct.getId(), "CartProductId should not be null");
        assertEquals(1, cartProduct.getId().getCartId(), "Cart ID should match");
        assertEquals(100, cartProduct.getId().getProductId(), "Product ID should match");

        assertEquals(cart, cartProduct.getCart(), "Cart should match");
        assertEquals(product, cartProduct.getProduct(), "Product should match");
    }

    @Test
    void testSetters() {
        CartProduct cartProduct = new CartProduct();

        Cart cart = new Cart();
        cart.setId(2);

        Product product = new Product();
        product.setId(200);

        cartProduct.setCart(cart);
        cartProduct.setProduct(product);
        cartProduct.setId(new CartProductId(cart.getId(), product.getId()));

        assertEquals(2, cartProduct.getId().getCartId());
        assertEquals(200, cartProduct.getId().getProductId());
        assertEquals(cart, cartProduct.getCart());
        assertEquals(product, cartProduct.getProduct());
    }
}
