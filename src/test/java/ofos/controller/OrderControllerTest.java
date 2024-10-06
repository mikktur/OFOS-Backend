package ofos.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import ofos.dto.OrderDTO;
import ofos.dto.OrderHistoryDTO;
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
        HashMap<Integer, List<OrderHistoryDTO>> hashMap = new HashMap<>();
        List<OrderHistoryDTO> orderHistoryList = new ArrayList<>();
        OrderHistoryDTO orderHistoryDTO = new OrderHistoryDTO(
                BigDecimal.valueOf(5),
                5,
                "Burgeri",
                new Date(143242421),
                "Ravintola Nimi"
        );
        orderHistoryList.add(orderHistoryDTO);
        hashMap.put(1, orderHistoryList);

        when(jwtUtil.extractUsername(any())).thenReturn("testUser");
        when(ordersService.getHistory(anyString())).thenReturn(hashMap);

        MvcResult mvcResult = mvc.perform(get("/api/order/history")
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
}