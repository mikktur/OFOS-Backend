package ofos.repository;

import ofos.entity.OrdersEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OrdersRepository extends JpaRepository<OrdersEntity, Long> {

    List<OrdersEntity> findOrdersEntitiesByUserId(int id);

    @Query(value = "SELECT Quantity, ProductPrice, ProductName, OrderID " +
            "FROM OrderProducts INNER JOIN Products ON OrderProducts.ProductID = Products.ProductID " +
            "WHERE OrderID IN (SELECT OrderID FROM Orders WHERE User_ID = ?1)", nativeQuery = true)
    List<IOrderHistory> getOrderHistory(int userID);

}
