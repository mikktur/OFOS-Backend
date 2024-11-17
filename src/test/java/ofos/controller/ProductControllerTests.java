package ofos.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import ofos.dto.ProductDTO;
import ofos.entity.ProductEntity;
import ofos.entity.UserEntity;
import ofos.security.JwtRequestFilter;
import ofos.security.JwtUtil;
import ofos.security.MyUserDetails;
import ofos.service.ProductService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import java.math.BigDecimal;


import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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
        String language = "en";
        ProductEntity productEntity = new ProductEntity();
        productEntity.setProductId(20);
        productEntity.setProductName("Good brgr");
        productEntity.setProductDesc("Hyvä burgeri.");
        productEntity.setPicture("https://cdn.rkt-prod.rakentaja.com/media/original_images/202212_60205.jpg");
        productEntity.setCategory("Hampurilainen");
        productEntity.setActive(true);

        when(productService.getDishById(20, language)).thenReturn(productEntity);

        mvc.perform(get("/api/products/" + language + "/20"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.productName").value(productEntity.getProductName()))
                .andExpect(jsonPath("$.productId").value(productEntity.getProductId()))
                .andDo(MockMvcResultHandlers.print());


    }

//    @Test
//    public void createProductTest() throws Exception {
//        ProductDTO productDTO = new ProductDTO(100,"Good brgr", "Maistuu namnam", BigDecimal.valueOf(17.50),
//                "Hampurilainen", "https://cdn.rkt-prod.rakentaja.com/media/original_images/202212_60205.jpg,", true);
//        ResponseEntity<String> responseEntity = ResponseEntity.ok("Product created.");
//
//        when(jwtUtil.extractRole(any())).thenReturn("Owner");
//        when(jwtUtil.extractUsername(any())).thenReturn("testUser");
//        when(productService.createProduct(any(ProductDTO.class), anyInt(), anyString())).thenReturn(responseEntity);
//
//        mvc.perform(post("/api/products/create/1")
//                .header("Authorization", "Bearer testToken")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(objectMapper.writeValueAsString(productDTO)))
//                .andExpect(status().isOk())
//                .andExpect(content().string("Product created."))
//                .andDo(MockMvcResultHandlers.print());
//    }

    @Test
    public void updateProductTest() throws Exception {
        ProductDTO productDTO = new ProductDTO(333, "Good brgr", "Maistuu namnam", BigDecimal.valueOf(17.50),
                "Hampurilainen", "https://cdn.rkt-prod.rakentaja.com/media/original_images/202212_60205.jpg");
        UserEntity userEntity = new UserEntity(1, "testUser", "testPassword", "Owner",true);
        MyUserDetails userDetails = new MyUserDetails(userEntity);

        ResponseEntity<String> responseEntity = ResponseEntity.ok("Product updated.");

        when(jwtUtil.extractRole(any())).thenReturn("Owner");
        when(jwtUtil.extractUsername(any())).thenReturn("testUser");
        when(productService.updateProduct(any(ProductDTO.class), anyInt(), anyInt())).thenReturn(responseEntity);

        Authentication auth = new UsernamePasswordAuthenticationToken(userDetails, null);
        SecurityContextHolder.getContext().setAuthentication(auth);

        mvc.perform(put("/api/products/update/1")
                .header("Authorization", "Bearer testToken")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(productDTO)))
                .andExpect(status().isOk())
                .andExpect(content().string("Product updated."))
                .andDo(MockMvcResultHandlers.print());
    }


}
