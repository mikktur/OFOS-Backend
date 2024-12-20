package ofos.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import ofos.dto.OrderDTO;
import ofos.dto.OrderHistoryDTO;
import ofos.entity.*;
import ofos.security.JwtUtil;
import ofos.service.CustomUserDetailsService;
import ofos.service.OrdersService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(OrderController.class)
@AutoConfigureMockMvc(addFilters = false)
class OrderControllerTest {

    @Autowired
    private MockMvc mvc;
    @MockBean
    private CustomUserDetailsService customUserDetailsService;
    @MockBean
    private OrdersService ordersService;

    @MockBean
    private JwtUtil jwtUtil;

    @Autowired
    private ObjectMapper objectMapper;


    @Test
    void makeOrderTest() throws Exception {
        List<OrderDTO> order = new ArrayList<>();
        order.add(new OrderDTO("Ordered", 5, 1, 1, 1));

        when(jwtUtil.extractUsername(any())).thenReturn("testUser");
        when(ordersService.postOrder(anyList(), anyString())).thenReturn(ResponseEntity.ok("Order placed."));

        mvc.perform(post("/api/order")
                .header("Authorization", "Bearer testToken")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(order)))
                .andExpect(status().isOk())
                .andExpect(content().string("Order placed."));
    }

    @Test
    void getOrdersByIDTest() throws Exception {
        RestaurantEntity restaurant = new RestaurantEntity();
        restaurant.setRestaurantID(1);
        UserEntity user = new UserEntity();
        user.setUserId(1);
        List<OrdersEntity> orders = new ArrayList<>();
        orders.add(new OrdersEntity(1, "Ordered", "Osoite", new java.sql.Date(1), user, restaurant));

        when(ordersService.getOrdersByUserID(anyInt())).thenReturn(orders);

        MvcResult mvcResult = mvc.perform(get("/api/order/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String contentAsString = mvcResult.getResponse().getContentAsString();
        List<OrdersEntity> returnedOrders = objectMapper.readValue(contentAsString, new TypeReference<List<OrdersEntity>>(){});

        assertEquals(1, returnedOrders.get(0).getUser().getUserId());
    }

    @Test
    void getOrderProductsByIdTest() throws Exception {
        ProductEntity product = new ProductEntity();
        product.setProductId(1);
        OrdersEntity order = new OrdersEntity();
        order.setOrderId(1);
        List<OrderProductsEntity> orderProducts = new ArrayList<>();
        orderProducts.add(new OrderProductsEntity(order, product, 2));

        when(ordersService.getOrderContentsByUserID(anyInt())).thenReturn(orderProducts);

        MvcResult mvcResult = mvc.perform(get("/api/order/products/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String contentAsString = mvcResult.getResponse().getContentAsString();
        List<OrderProductsEntity> returnedOrderProducts = objectMapper.readValue(contentAsString, new TypeReference<List<OrderProductsEntity>>(){});

        assertEquals(1, returnedOrderProducts.get(0).getOrder().getOrderId());
        assertEquals(1, returnedOrderProducts.get(0).getProduct().getProductId());
    }

    @Test
    void getOrderHistoryTest() throws Exception {
        HashMap<Integer, List<OrderHistoryDTO>> hashMap = new HashMap<>();
        List<OrderHistoryDTO> orderHistoryList = new ArrayList<>();
        OrderHistoryDTO orderHistoryDTO = new OrderHistoryDTO(
                BigDecimal.valueOf(5),
                5,
                "Burgeri",
                new Date(143242421),
                "Ravintola Nimi",
                1
        );
        orderHistoryList.add(orderHistoryDTO);
        hashMap.put(1, orderHistoryList);

        when(jwtUtil.extractUsername(any())).thenReturn("testUser");
        when(ordersService.getHistory(anyString(), anyString())).thenReturn(hashMap);

        MvcResult mvcResult = mvc.perform(get("/api/order/fi/history")
                        .header("Authorization", "Bearer testToken")
                        .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk())
                        .andReturn();

        String contentAsString = mvcResult.getResponse().getContentAsString();
        HashMap<Integer, List<OrderHistoryDTO>> returnedOrderHistory = objectMapper.readValue(contentAsString, new TypeReference<HashMap<Integer, List<OrderHistoryDTO>>>(){});

        assertEquals(hashMap.size(), returnedOrderHistory.size());

        for (Integer key : hashMap.keySet()) {
            assertTrue(returnedOrderHistory.containsKey(key));
            assertEquals(hashMap.get(key).size(), returnedOrderHistory.get(key).size());

            for (int i = 0; i < hashMap.get(key).size(); i++) {
                OrderHistoryDTO expected = hashMap.get(key).get(i);
                OrderHistoryDTO returned = returnedOrderHistory.get(key).get(i);

                assertEquals(expected.getOrderPrice(), returned.getOrderPrice());
                assertEquals(expected.getQuantity(), returned.getQuantity());
                assertEquals(expected.getProductName(), returned.getProductName());
                assertEquals(expected.getOrderDate().toString(), returned.getOrderDate().toString());
            }
        }
    }

    @Test
    void makeOrderFailureTest() throws Exception {
        List<OrderDTO> order = new ArrayList<>();
        order.add(new OrderDTO("Ordered", 5, 1, 1, 1));

        when(jwtUtil.extractUsername(any())).thenReturn("testUser");
        when(ordersService.postOrder(anyList(), anyString())).thenReturn(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Order failed."));

        mvc.perform(post("/api/order")
                .header("Authorization", "Bearer testToken")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(order)))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("Order failed."));
    }

    @Test
    void getOrdersByIDFailureTest() throws Exception {
        when(ordersService.getOrdersByUserID(anyInt())).thenReturn(null);

        MvcResult mvcResult = mvc.perform(get("/api/order/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        String responseBody = mvcResult.getResponse().getContentAsString();
        assertEquals("", responseBody);
    }

    @Test
    void getOrderProductsByIDFailureTest() throws Exception {
        when(ordersService.getOrderContentsByUserID(anyInt())).thenReturn(null);

        MvcResult mvcResult = mvc.perform(get("/api/order/products/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        String responseBody = mvcResult.getResponse().getContentAsString();
        assertEquals("", responseBody);
    }

    @Test
    void getOrderHistoryFailureTest() throws Exception {
        when(jwtUtil.extractUsername(any())).thenReturn("testUser");
        when(ordersService.getHistory(anyString(), anyString())).thenReturn(null);


        MvcResult mvcResult = mvc.perform(get("/api/order/fi/history")
                .header("Authorization", "Bearer testToken")
                .contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        String responseBody = mvcResult.getResponse().getContentAsString();
        assertEquals("", responseBody);
    }

    @Test
    void updateStatusFailureTest() throws Exception {
        when(ordersService.updateStatus(anyInt(), anyString())).thenReturn(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Update status failed."));

        mvc.perform(post("/api/order/status/1/failed")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("Update status failed."));
    }
}