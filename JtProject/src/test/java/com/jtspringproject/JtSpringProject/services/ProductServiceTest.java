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
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

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
}
