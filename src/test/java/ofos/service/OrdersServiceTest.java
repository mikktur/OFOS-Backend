package ofos.service;

import ofos.dto.OrderDTO;
import ofos.dto.OrderHistoryDTO;
import ofos.entity.*;
import ofos.repository.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class OrdersServiceTest {

    @Mock
    private OrdersRepository ordersRepository;
    @Mock
    private OrderProductsRepository orderProductsRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private DeliveryAddressRepository deliveryAddressRepository;
    @Mock
    private RestaurantRepository restaurantRepository;
    @Mock
    private ProductRepository productRepository;
    @InjectMocks
    private OrdersService ordersService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getOrdersByUserIDTest() {
        int userId = 1;
        List<OrdersEntity> expectedOrders = Collections.singletonList(new OrdersEntity());
        when(ordersRepository.findOrdersEntitiesByUser_UserId(userId)).thenReturn(expectedOrders);

        List<OrdersEntity> actualOrders = ordersService.getOrdersByUserID(userId);

        assertEquals(expectedOrders, actualOrders);
        verify(ordersRepository).findOrdersEntitiesByUser_UserId(userId);
    }

    @Test
    void getOrderContentsByUserIDTest() {
        int userId = 1;
        List<OrderProductsEntity> expectedOrderProducts = Collections.singletonList(new OrderProductsEntity());
        when(orderProductsRepository.findOrdersByUserID(userId)).thenReturn(expectedOrderProducts);

        List<OrderProductsEntity> actualOrderProducts = ordersService.getOrderContentsByUserID(userId);

        assertEquals(expectedOrderProducts, actualOrderProducts);
        verify(orderProductsRepository).findOrdersByUserID(userId);
    }

    @Test
    void postOrderTest() {
        List<OrderDTO> orders = new ArrayList<>();
        OrderDTO orderDTO = new OrderDTO(
                "Preparing order",
                3,
                1,
                1,
                1
        );
        orders.add(orderDTO);

        String username = "test";

        UserEntity userEntity = new UserEntity();
        userEntity.setUserId(1);

        DeliveryAddressEntity deliveryAddressEntity = new DeliveryAddressEntity();
        deliveryAddressEntity.setStreetAddress("Osoite 1");

        RestaurantEntity restaurantEntity = new RestaurantEntity();
        restaurantEntity.setRestaurantID(1);

        ProductEntity productEntity = new ProductEntity();
        productEntity.setProductId(1);

        OrdersEntity ordersEntity = new OrdersEntity();
        ordersEntity.setOrderId(1);
        ordersEntity.setRestaurant(restaurantEntity);
        ordersEntity.setUser(userEntity);


        when(userRepository.findByUsername(username)).thenReturn(userEntity);
        when(deliveryAddressRepository.findById(anyInt())).thenReturn(Optional.of(deliveryAddressEntity));
        when(restaurantRepository.findById(anyInt())).thenReturn(Optional.of(restaurantEntity));
        when(productRepository.findAllById(anyList())).thenReturn(List.of(productEntity));
        when(ordersRepository.save(any(OrdersEntity.class))).thenReturn(ordersEntity);

        ResponseEntity<String> responseEntity = ordersService.postOrder(orders, username);


        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("Order placed successfully", responseEntity.getBody());

        verify(userRepository).findByUsername(username);
        verify(deliveryAddressRepository).findById(anyInt());
        verify(restaurantRepository).findById(anyInt());
        verify(productRepository).findAllById(anyList());
        verify(ordersRepository).save(any(OrdersEntity.class));
    }

    @Test
    void getHistoryTest() {
        OrdersEntity mockOrder = new OrdersEntity();
        mockOrder.setOrderId(1);
        mockOrder.setOrderDate(Date.valueOf("2024-01-01"));

        OrderProductsEntity mockOrderProduct = new OrderProductsEntity();
        mockOrderProduct.setQuantity(2);

        ProductEntity mockProduct = new ProductEntity();
        mockProduct.setProductId(101);
        mockProduct.setProductPrice(BigDecimal.valueOf(9.99));

        TranslationEntity mockTranslation = new TranslationEntity();
        mockTranslation.setName("Pizza");

        RestaurantEntity mockRestaurant = new RestaurantEntity();
        mockRestaurant.setRestaurantName("Italian Delight");

        Object[] result = new Object[]{mockOrder, mockOrderProduct, mockProduct, mockTranslation, mockRestaurant};


        Mockito.when(ordersRepository.findOrdersByUsername("testuser", "en"))
                .thenReturn(Collections.singletonList(result));


        HashMap<Integer, List<OrderHistoryDTO>> history = ordersService.getHistory("testuser", "en");


        Assertions.assertEquals(1, history.size(), "History should contain one order");
        Assertions.assertTrue(history.containsKey(1), "History should contain order ID 1");

        List<OrderHistoryDTO> orderHistoryList = history.get(1);
        Assertions.assertEquals(1, orderHistoryList.size(), "Order ID 1 should have one entry");

        OrderHistoryDTO dto = orderHistoryList.get(0);
        Assertions.assertEquals(9.99, dto.getOrderPrice().doubleValue(), 0.01);
        Assertions.assertEquals(2, dto.getQuantity());
        Assertions.assertEquals("Pizza", dto.getProductName());
        Assertions.assertEquals(mockOrder.getOrderDate(), dto.getOrderDate());
        Assertions.assertEquals("Italian Delight", dto.getRestaurantName());
        Assertions.assertEquals(101, dto.getProductId());
    }

    @Test
    void updateStatusTest() {
        int orderId = 1;
        String status = "Delivered";

        ResponseEntity<String> responseEntity = ordersService.updateStatus(orderId, status);

        assertEquals(ResponseEntity.ok("Status updated to: " + status), responseEntity);
        verify(ordersRepository).updateByOrderId(orderId, status);
    }
}