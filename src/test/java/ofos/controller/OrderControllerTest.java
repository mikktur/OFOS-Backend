package ofos.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import ofos.dto.OrderDTO;
import ofos.entity.OrderProductsEntity;
import ofos.entity.OrdersEntity;
import ofos.repository.IOrderHistory;
import ofos.security.JwtUtil;
import ofos.service.OrdersService;
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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
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
    private OrdersService ordersService;

    @MockBean
    private JwtUtil jwtUtil;

    @Autowired
    private ObjectMapper objectMapper;

    @Mock
    private HttpServletRequest request;

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
        List<OrdersEntity> orders = new ArrayList<>();
        orders.add(new OrdersEntity(1, "Ordered", "Osoite", new java.sql.Date(1), 1, 1));

        when(ordersService.getOrdersByUserID(anyInt())).thenReturn(orders);

        MvcResult mvcResult = mvc.perform(get("/api/order/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String contentAsString = mvcResult.getResponse().getContentAsString();
        List<OrdersEntity> returnedOrders = objectMapper.readValue(contentAsString, new TypeReference<List<OrdersEntity>>(){});

        assertEquals(1, returnedOrders.get(0).getUserId());
    }

    @Test
    void getOrderProductsByIdTest() throws Exception {
        List<OrderProductsEntity> orderProducts = new ArrayList<>();
        orderProducts.add(new OrderProductsEntity(1, 1, 2));

        when(ordersService.getOrderContentsByUserID(anyInt())).thenReturn(orderProducts);

        MvcResult mvcResult = mvc.perform(get("/api/order/products/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String contentAsString = mvcResult.getResponse().getContentAsString();
        List<OrderProductsEntity> returnedOrderProducts = objectMapper.readValue(contentAsString, new TypeReference<List<OrderProductsEntity>>(){});

        assertEquals(1, returnedOrderProducts.get(0).getOrderId());
        assertEquals(1, returnedOrderProducts.get(0).getProductId());
    }

    @Test
    void getOrderHistoryTest() throws Exception {
        List<IOrderHistory> orderHistoryList = new ArrayList<>();
        orderHistoryList.add(new OrderHistoryImpl(1, BigDecimal.valueOf(5), 5, "Burgeri", new Date(1)));

        when(jwtUtil.extractUsername(any())).thenReturn("testUser");
        when(ordersService.getHistory(anyString())).thenReturn(orderHistoryList);

        MvcResult mvcResult = mvc.perform(get("/api/order/history")
                .header("Authorization", "Bearer testToken")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String contentAsString = mvcResult.getResponse().getContentAsString();

        List<OrderHistoryImpl> returnedOrderHistory = objectMapper.readValue(contentAsString, new TypeReference<List<OrderHistoryImpl>>(){});

        assertEquals(orderHistoryList.size(), returnedOrderHistory.size());
        for (int i = 0; i < orderHistoryList.size(); i++) {
            assertEquals(orderHistoryList.get(i).getOrderID(), returnedOrderHistory.get(i).getOrderID());
            assertEquals(0, orderHistoryList.get(i).getProductPrice().compareTo(returnedOrderHistory.get(i).getProductPrice()));
            assertEquals(orderHistoryList.get(i).getQuantity(), returnedOrderHistory.get(i).getQuantity());
            assertEquals(orderHistoryList.get(i).getProductName(), returnedOrderHistory.get(i).getProductName());
        }
    }
}