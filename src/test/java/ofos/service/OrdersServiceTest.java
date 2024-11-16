package ofos.service;

import ofos.dto.OrderDTO;
import ofos.entity.*;
import ofos.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
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
        deliveryAddressEntity.setCity("Kaupunki");
        deliveryAddressEntity.setInfo("Ovikoodi: 777");
        deliveryAddressEntity.setPostalCode("00220");
        OrdersEntity ordersEntity = new OrdersEntity();
        ordersEntity.setOrderDate(new Date(1));
        ordersEntity.setOrderAddress("Osoite 1");
        ordersEntity.setState("Preparing order");
        ordersEntity.setOrderId(1);
        RestaurantEntity restaurantEntity = new RestaurantEntity();
        restaurantEntity.setRestaurantID(1);
        ordersEntity.setRestaurant(restaurantEntity);
        UserEntity user = new UserEntity();
        user.setUserId(1);
        ordersEntity.setUser(user);

        when(userRepository.findByUsername(username)).thenReturn(userEntity);
        when(deliveryAddressRepository.getByDeliveryAddressId(anyInt())).thenReturn(deliveryAddressEntity);
        when(ordersRepository.save(any(OrdersEntity.class))).thenReturn(ordersEntity);
        when(restaurantRepository.findById(anyInt())).thenReturn(java.util.Optional.of(restaurantEntity));
        ResponseEntity<String> responseEntity = ordersService.postOrder(orders, username);

        assertEquals(ResponseEntity.ok("Order received."), responseEntity);
        verify(userRepository).findByUsername(username);
        verify(ordersRepository).save(any(OrdersEntity.class));
        verify(orderProductsRepository, times(orders.size())).save(any(OrderProductsEntity.class));
    }

    @Test
    void getHistoryTest() {
        String username = "test";
        UserEntity userEntity = new UserEntity();
        userEntity.setUserId(1);
        when(userRepository.findByUsername(username)).thenReturn(userEntity);

        ordersService.getHistory(username, "fi");

        verify(userRepository).findByUsername(username);
        verify(ordersRepository).findOrdersEntitiesByUser_UserId(userEntity.getUserId());
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