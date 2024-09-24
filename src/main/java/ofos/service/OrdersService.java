package ofos.service;

import ofos.dto.OrderDTO;
import ofos.entity.OrderProductsEntity;
import ofos.entity.OrdersEntity;
import ofos.entity.UserEntity;
import ofos.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

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





    public List<OrdersEntity> getOrdersByUserID(int userID){
        return ordersRepository.findOrdersEntitiesByUserId(userID);
    }

    public List<OrderProductsEntity> getOrderContentsByUserID(int userID){
        return orderProductsRepository.findOrdersByUserID(userID);
    }

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

    public List<IOrderHistory> getHistory(String username){
        int userID = userRepository.findByUsername(username).getUserId();
        return ordersRepository.getOrderHistory(userID);
    }

}
