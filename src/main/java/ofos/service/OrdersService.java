package ofos.service;

import ofos.dto.OrderDTO;
import ofos.dto.OrderHistoryDTO;
import ofos.entity.*;
import ofos.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;


@Service
public class OrdersService {

    @Autowired
    OrdersRepository ordersRepository;

    @Autowired
    OrderProductsRepository orderProductsRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    DeliveryAddressRepository deliveryAddressRepository;

    @Autowired
    RestaurantRepository restaurantRepository;

    @Autowired
    private ProductRepository productRepository;



    public List<OrdersEntity> getOrdersByUserID(int userID) {
        return ordersRepository.findOrdersEntitiesByUser_UserId(userID);
    }


    public List<OrderProductsEntity> getOrderContentsByUserID(int userID) {
        return orderProductsRepository.findOrdersByUserID(userID);
    }

    //TODO return Statuscdoes instead of runtime exceptions
    @Transactional
    public ResponseEntity<String> postOrder(List<OrderDTO> orders, String username) {
        UserEntity user = userRepository.findByUsername(username);
        if (user == null) {
            return new ResponseEntity<>("User not found", HttpStatus.BAD_REQUEST);
        }

        if (orders == null || orders.isEmpty()) {
            return new ResponseEntity<>("Order list is empty", HttpStatus.BAD_REQUEST);
        }

        RestaurantEntity restaurant = restaurantRepository.findById(orders.get(0).getRestaurantID())
                .orElseThrow(() -> new RuntimeException("Restaurant not found"));

        String deliveryAddress = deliveryAddressRepository.findById(orders.get(0).getDeliveryAddressID())
                .orElseThrow(() -> new RuntimeException("Delivery address not found"))
                .getStreetAddress();

        OrdersEntity order = new OrdersEntity();
        order.setUser(user);
        order.setRestaurant(restaurant);
        order.setOrderAddress(deliveryAddress);
        order.setState("dunno");

        Map<Integer, ProductEntity> products = getProductsMappedById(orders);

        for (OrderDTO orderDTO : orders) {
            ProductEntity product = products.get(orderDTO.getProductID());
            if (product == null) {
                throw new RuntimeException("Product not found for ID: " + orderDTO.getProductID());
            }

            OrderProductsEntity orderProduct = new OrderProductsEntity(order, product, orderDTO.getQuantity());
            order.getOrderProducts().add(orderProduct);
        }
        ordersRepository.save(order);

        return new ResponseEntity<>("Order placed successfully", HttpStatus.OK);
    }


    private Map<Integer, ProductEntity> getProductsMappedById(List<OrderDTO> orders) {
        List<Integer> productIds = orders.stream()
                .map(OrderDTO::getProductID)
                .distinct()
                .collect(Collectors.toList());

        return productRepository.findAllById(productIds)
                .stream()
                .collect(Collectors.toMap(ProductEntity::getProductId, Function.identity()));
    }

    @Transactional
    public HashMap<Integer, List<OrderHistoryDTO>> getHistory(String username, String language) {
        List<Object[]> results = ordersRepository.findOrdersByUsername(username,language);

        HashMap<Integer, List<OrderHistoryDTO>> history = new HashMap<>();

        for (Object[] result : results) {
            OrdersEntity order = (OrdersEntity) result[0];
            OrderHistoryDTO dto = getOrderHistoryDTO(result, order);

            history.computeIfAbsent(order.getOrderId(), k -> new ArrayList<>()).add(dto);
        }

        return history;
    }


    private static OrderHistoryDTO getOrderHistoryDTO(Object[] result, OrdersEntity order) {
        OrderProductsEntity orderProduct = (OrderProductsEntity) result[1];
        ProductEntity product = (ProductEntity) result[2];
        TranslationEntity translation = (TranslationEntity) result[3];
        RestaurantEntity restaurant = (RestaurantEntity) result[4];
        return new OrderHistoryDTO(
                product.getProductPrice(),
                orderProduct.getQuantity(),
                translation.getName(),
                order.getOrderDate(),
                restaurant.getRestaurantName(),
                product.getProductId()
        );
    }


    public ResponseEntity<String> updateStatus(int orderID, String status) {
        try {
            ordersRepository.updateByOrderId(orderID, status);
            return new ResponseEntity<>(
                    "Status updated to: " + status,
                    HttpStatus.OK
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                    "Something went wrong.",
                    HttpStatus.BAD_REQUEST
            );
        }
    }

}
