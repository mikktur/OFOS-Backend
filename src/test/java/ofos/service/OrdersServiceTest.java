package ofos.service;

import ofos.dto.OrderDTO;
import ofos.dto.OrderHistoryDTO;
import ofos.entity.*;
import ofos.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
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
        String username = "testUser";
        String language = "en";


        UserEntity userEntity = new UserEntity();
        userEntity.setUserId(1);

        RestaurantEntity restaurantEntity = new RestaurantEntity();
        restaurantEntity.setRestaurantID(1);
        restaurantEntity.setRestaurantName("Mock Restaurant");

        ProductEntity productEntity = new ProductEntity();
        productEntity.setProductId(1);
        productEntity.setProductName("Mock Product");
        productEntity.setProductPrice(BigDecimal.valueOf(10.00));

        OrderProductsEntity orderProductsEntity = new OrderProductsEntity();
        orderProductsEntity.setProduct(productEntity);
        orderProductsEntity.setQuantity(2);

        OrdersEntity ordersEntity = new OrdersEntity();
        ordersEntity.setOrderId(1);
        ordersEntity.setRestaurant(restaurantEntity);
        ordersEntity.setOrderProducts(List.of(orderProductsEntity));

        userEntity.setOrders(List.of(ordersEntity));


        when(userRepository.findByUsername(username)).thenReturn(userEntity);


        HashMap<Integer, List<OrderHistoryDTO>> result = ordersService.getHistory(username, language);

        assertNotNull(result);
        assertTrue(result.containsKey(1));
        List<OrderHistoryDTO> orderHistory = result.get(1);
        assertNotNull(orderHistory);
        assertEquals(1, orderHistory.size());

        OrderHistoryDTO dto = orderHistory.get(0);
        assertEquals(productEntity.getProductName(), dto.getProductName());
        assertEquals(productEntity.getProductPrice(), dto.getOrderPrice());
        assertEquals(restaurantEntity.getRestaurantName(), dto.getRestaurantName());
        assertEquals(ordersEntity.getOrderDate(), dto.getOrderDate());
        assertEquals(orderProductsEntity.getQuantity(), dto.getQuantity());


        verify(userRepository).findByUsername(username);
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