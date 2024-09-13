package ofos.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import ofos.dto.ProductDTO;
import ofos.entity.ProductEntity;
import ofos.security.JwtRequestFilter;
import ofos.security.JwtUtil;
import ofos.service.ProductService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(ProductController.class)
@AutoConfigureMockMvc(addFilters = false)
public class ProductControllerTests {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private ProductService productService;

    @MockBean
    private JwtRequestFilter jwtRequestFilter;

    @MockBean
    private JwtUtil jwtUtil;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void getProductByIdTest() throws Exception {
        ProductEntity productEntity = new ProductEntity();
        productEntity.setProductId(20);
        productEntity.setProductName("Good brgr");
        productEntity.setProductDesc("Hyvä burgeri.");
        productEntity.setPicture("https://cdn.rkt-prod.rakentaja.com/media/original_images/202212_60205.jpg");
        productEntity.setCategory("Hampurilainen");
        productEntity.setActive(true);

        when(productService.getDishById(20)).thenReturn(productEntity);

        mvc.perform(get("/api/products/20"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.productName").value(productEntity.getProductName()))
                .andExpect(jsonPath("$.productId").value(productEntity.getProductId()))
                .andDo(MockMvcResultHandlers.print());


    }

    @Test
    public void createProductTest() throws Exception {
        ProductDTO productDTO = new ProductDTO("Good brgr", "Maistuu namnam", BigDecimal.valueOf(17.50),
                "Hampurilainen", "https://cdn.rkt-prod.rakentaja.com/media/original_images/202212_60205.jpg");

        mvc.perform(post("/api/products/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(productDTO)))
                .andExpect(status().isOk())
                .andExpect(content().string("Product added."))
                .andDo(MockMvcResultHandlers.print());

    }

    @Test
    public void updateProductTest() throws Exception {
        ProductDTO productDTO = new ProductDTO(333, "Good brgr", "Maistuu namnam", BigDecimal.valueOf(17.50),
                "Hampurilainen", "https://cdn.rkt-prod.rakentaja.com/media/original_images/202212_60205.jpg");
        ProductEntity updatedProductEntity = new ProductEntity();
        updatedProductEntity.setProductName("Good brgr");
        updatedProductEntity.setProductDesc("Epämaistuu hyihyi.");
        updatedProductEntity.setProductPrice(BigDecimal.valueOf(10.39));
        updatedProductEntity.setCategory("Hampurilainen");
        updatedProductEntity.setPicture("https://cdn.rkt-prod.rakentaja.com/media/original_images/202212_60205.jpg");

        // TODO:
        // Metodi päivitetty palauttamaan response entityn eikä product entityä
        //when(productService.updateProduct(productDTO)).thenReturn(updatedProductEntity);

        mvc.perform(post("/api/products/update")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(productDTO)))
                .andExpect(status().isOk())
                .andExpect(content().string("Product updated."))
                .andDo(MockMvcResultHandlers.print());

    }

}
