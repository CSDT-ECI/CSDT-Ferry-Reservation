package com.jtspringproject.JtSpringProject.services;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.*;

import com.jtspringproject.JtSpringProject.dao.productDao;
import com.jtspringproject.JtSpringProject.models.Product;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private productDao productDao;

    @InjectMocks
    private productService productService;

    @Test
    void getProductsReturnsDaoList() {
        Product product = new Product();
        product.setName("Ticket A");
        List<Product> expected = Collections.singletonList(product);

        when(productDao.getProducts()).thenReturn(expected);

        List<Product> result = productService.getProducts();

        assertEquals(1, result.size());
        assertEquals("Ticket A", result.get(0).getName());
        verify(productDao).getProducts();
    }

    @Test
    void updateProductSetsPathIdBeforeDaoUpdate() {
        Product incoming = new Product();
        incoming.setId(0);
        incoming.setName("New name");

        when(productDao.updateProduct(incoming)).thenReturn(incoming);

        Product result = productService.updateProduct(25, incoming);

        ArgumentCaptor<Product> captor = ArgumentCaptor.forClass(Product.class);
        verify(productDao).updateProduct(captor.capture());

        assertEquals(25, captor.getValue().getId());
        assertSame(incoming, result);
    }

    @Test
    void deleteProductDelegatesToDao() {
        when(productDao.deletProduct(8)).thenReturn(true);

        boolean deleted = productService.deleteProduct(8);

        assertEquals(true, deleted);
        verify(productDao).deletProduct(8);
    }
    @Test
void addProductDelegatesToDao() {
    Product product = new Product();
    when(productDao.addProduct(product)).thenReturn(product);

    Product result = productService.addProduct(product);

    assertSame(product, result);
    verify(productDao).addProduct(product);
}
@Test
void getProductReturnsFromDao() {
    Product product = new Product();
    when(productDao.getProduct(1)).thenReturn(product);

    Product result = productService.getProduct(1);

    assertEquals(product, result);
}

@Test
void getProductReturnsDaoResult() {
    Product product = new Product();
    product.setId(1);

    when(productDao.getProduct(1)).thenReturn(product);

    Product result = productService.getProduct(1);

    assertNotNull(result);
    assertEquals(1, result.getId());
    verify(productDao).getProduct(1);
}
}
