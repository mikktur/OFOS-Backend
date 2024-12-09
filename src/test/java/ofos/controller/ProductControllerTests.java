package ofos.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import ofos.dto.ProductDTO;
import ofos.entity.ProductEntity;
import ofos.entity.UserEntity;
import ofos.repository.RestaurantRepository;
import ofos.security.JwtRequestFilter;
import ofos.security.JwtUtil;
import ofos.security.MyUserDetails;
import ofos.service.ProductService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
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
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import java.math.BigDecimal;


import static org.junit.jupiter.api.Assertions.assertEquals;
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

    @MockBean
    private RestaurantRepository restaurantRepository;


    @Test
    public void getProductByIdTest() throws Exception {
        String language = "en";
        ProductDTO productDTO = new ProductDTO(20, "Good brgr", "Maistuu namnam", BigDecimal.valueOf(17.50),
                "Hampurilainen", "https://cdn.rkt-prod.rakentaja.com/media/original_images/202212_60205.jpg");

        when(productService.getDishById(20)).thenReturn(productDTO);

        mvc.perform(get("/api/products/20"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.productName").value(productDTO.getProductName()))
                .andExpect(jsonPath("$.productID").value(productDTO.getProductID()))
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

    @Test
    public void getProductByIdFailureTest() throws Exception {
        when(productService.getDishById(anyInt())).thenReturn(null);


        MvcResult mvcResult = mvc.perform(get("/api/products/en/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        String responseBody = mvcResult.getResponse().getContentAsString();
        assertEquals("", responseBody);
    }

    @Test
    public void deleteDishFailureTest() throws Exception {
        when(jwtUtil.extractRole(anyString())).thenReturn("USER");

        mvc.perform(delete("/api/products/delete/1")
                        .header("Authorization", "Bearer testToken")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string("Not an owner."));
    }

    @Test
    public void deleteDishFromRestaurantFailureTest() throws Exception {
        when(jwtUtil.extractRole(anyString())).thenReturn("USER");

        mvc.perform(delete("/api/products/delete/1/restaurant/1")
                .header("Authorization", "Bearer testToken")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string("Not an owner."));
    }


}
