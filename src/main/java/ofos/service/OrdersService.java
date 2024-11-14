package ofos.service;

import ofos.dto.OrderDTO;
import ofos.dto.OrderHistoryDTO;
import ofos.entity.*;
import ofos.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;

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




    /**
     * Retrieves all orders related to certain user from the database.
     *
     * @param userID The ID of the user.
     *
     * @return A list of {@link OrdersEntity} objects representing all orders in the database.
     */
    public List<OrdersEntity> getOrdersByUserID(int userID){
        return ordersRepository.findOrdersEntitiesByUserId(userID);
    }

    /**
     * Retrieves all order contents related to certain user from the database.
     * @param userID The ID of the user.
     * @return A list of {@link OrderProductsEntity} objects representing all related order contents in the database.
     */
    public List<OrderProductsEntity> getOrderContentsByUserID(int userID){
        return orderProductsRepository.findOrdersByUserID(userID);
    }

    /**
     * Posts an order to the database.
     * @param orders list of {@link OrderDTO} objects representing the order to be posted.
     * @param username The username of the user making the order.
     * @return {@link ResponseEntity} object with a message and a status code.
     */
    public ResponseEntity<String> postOrder(List<OrderDTO> orders, String username){
        Date date = new Date();
        UserEntity user = userRepository.findByUsername(username);
        String orderAddress = deliveryAddressRepository.getByDeliveryAddressId(orders.get(0).getDeliveryAddressID()).getStreetAddress();
        long currentDate = date.getTime();
        java.sql.Date sqlDate = new java.sql.Date(currentDate);


        OrdersEntity ordersEntity = new OrdersEntity();
        ordersEntity.setOrderAddress(orderAddress);
        ordersEntity.setUserId(user.getUserId());
        ordersEntity.setRestaurantId(orders.get(0).getRestaurantID());
        ordersEntity.setOrderDate(sqlDate);
        ordersEntity.setState("Ordered");


        OrdersEntity savedEntity = ordersRepository.save(ordersEntity);

        orders.forEach(order -> {
            OrderProductsEntity product = new OrderProductsEntity();
            product.setOrderId(savedEntity.getOrderId());
            product.setProductId(order.getProductID());
            product.setQuantity(order.getQuantity());
            orderProductsRepository.save(product);
        });

        return new ResponseEntity<>(
                "Order received.",
                HttpStatus.OK
        );
    }


    /**
     * Retrieves the order history of a user from the database.
     * @param username The username of the user.
     * @return A HashMap with the order ID as the key and a list of {@link OrderHistoryDTO} objects as the value.
     */
    // TODO: Refaktoroi paska koodi pienempiin osiin
    // TODO: Loopissa ei varmaan kannata tehdä useaa tietokantakyselyä
    public HashMap<Integer, List<OrderHistoryDTO>> getHistory(String username, String language){
        HashMap<Integer, List<OrderHistoryDTO>> orders = new HashMap<>();
        int userID = userRepository.findByUsername(username).getUserId();
        List <IOrderHistory> orderHistory =ordersRepository.getOrderHistory(userID);

        for (int i = 0; i < orderHistory.size(); i++) {
            List<OrderHistoryDTO> orderProducts = new ArrayList<>();
            TranslationEntity translationEntity;
            int orderID = orderHistory.get(i).getOrderID();
            String restaurantName = "";
            int nextOrderID;
            Optional<RestaurantEntity> restaurant = restaurantRepository.findById(orderHistory.get(i).getRestaurantID());


            
            if (restaurant.isPresent()) {
                restaurantName = restaurant.get().getRestaurantName();
            }


            do {
                if (language.equals("fi")){
                    translationEntity = new TranslationEntity(orderHistory.get(i).getProductDesc(), orderHistory.get(i).getProductName());
                }
                else {
                    translationEntity = translationRepository.findByProductIdAndLang(
                            orderHistory.get(i).getProductID(), language);
                }
                if (translationEntity == null){
                    throw new RuntimeException("Product has no translations.");
                }
                OrderHistoryDTO orderHistoryDTO = new OrderHistoryDTO(
                        orderHistory.get(i).getProductPrice(),
                        orderHistory.get(i).getQuantity(),
                        translationEntity.getName(),
                        orderHistory.get(i).getOrderDate(),
                        restaurantName,
                        translationEntity.getDescription(),
                        translationEntity.getProductId()
                );

                orderProducts.add(orderHistoryDTO);
                i++;

                if (i == orderHistory.size()){
                    break;
                }

                nextOrderID = orderHistory.get(i).getOrderID();

            } while (orderID == nextOrderID);

            i--;
            orders.put(orderID, orderProducts);
        }
        return orders;
    }

    /**
     * Updates the status of an order in the database.
     * @param orderID The ID of the order.
     * @param status The new status of the order.
     * @return {@link ResponseEntity} object with a message and a status code.
     */
    public ResponseEntity<String> updateStatus(int orderID, String status){
        try {
            ordersRepository.updateByOrderId(orderID, status);
            return new ResponseEntity<>(
                    "Status updated to: " + status,
                    HttpStatus.OK
            );
        }
        catch (Exception e) {
            return new ResponseEntity<>(
                    "Something went wrong.",
                    HttpStatus.BAD_REQUEST
            );
        }
    }

}
