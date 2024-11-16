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

/**
 * This class provides methods to interact with and save the order data stored in the database.
 */
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
    TranslationRepository translationRepository;
    @Autowired
    private ProductRepository productRepository;


    /**
     * Retrieves all orders related to certain user from the database.
     *
     * @param userID The ID of the user.
     * @return A list of {@link OrdersEntity} objects representing all orders in the database.
     */
    public List<OrdersEntity> getOrdersByUserID(int userID) {
        return ordersRepository.findOrdersEntitiesByUser_UserId(userID);
    }

    /**
     * Retrieves all order contents related to certain user from the database.
     *
     * @param userID The ID of the user.
     * @return A list of {@link OrderProductsEntity} objects representing all related order contents in the database.
     */
    public List<OrderProductsEntity> getOrderContentsByUserID(int userID) {
        return orderProductsRepository.findOrdersByUserID(userID);
    }

    /**
     * Posts an order to the database.
     *
     * @param orders   list of {@link OrderDTO} objects representing the order to be posted.
     * @param username The username of the user making the order.
     * @return {@link ResponseEntity} object with a message and a status code.
     */
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
        UserEntity user = userRepository.findByUsername(username);
        if (user == null) {
            return null;
        }
        HashMap<Integer, List<OrderHistoryDTO>> history = new HashMap<>();
        List<OrdersEntity> orders = user.getOrders();

        for (OrdersEntity order : orders) {
            System.out.println(order.getOrderId());
            List<OrderHistoryDTO> productsList = new ArrayList<>();
            List<OrderProductsEntity> orderProducts = order.getOrderProducts();
            String restaurantName = order.getRestaurant().getRestaurantName();
            for (OrderProductsEntity orderProduct : orderProducts) {
                System.out.println(orderProduct.getProduct().getProductName());
                OrderHistoryDTO dto = new OrderHistoryDTO(
                        orderProduct.getProduct().getProductPrice(),
                        orderProduct.getQuantity(),
                        orderProduct.getProduct().getProductName(),
                        order.getOrderDate(),
                        restaurantName,
                        orderProduct.getProduct().getProductId()

                );
                productsList.add(dto);
            }
            history.put(order.getOrderId(), productsList);
        }


        return history;
    }

    /**
     * Updates the status of an order in the database.
     *
     * @param orderID The ID of the order.
     * @param status  The new status of the order.
     * @return {@link ResponseEntity} object with a message and a status code.
     */
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
