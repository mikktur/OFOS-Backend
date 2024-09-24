package ofos.repository;

import ofos.entity.OrderProductsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OrderProductsRepository extends JpaRepository<OrderProductsEntity, Long> {

    @Query(value = "SELECT * FROM OrderProducts WHERE OrderID IN " +
            "(SELECT OrderID FROM Orders WHERE User_ID = ?1)", nativeQuery = true)
    List<OrderProductsEntity> findOrdersByUserID(int userID);

}
