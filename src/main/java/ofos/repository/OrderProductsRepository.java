package ofos.repository;

import ofos.entity.OrderProductsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Repository for the {@link OrderProductsEntity} class.
 */
public interface OrderProductsRepository extends JpaRepository<OrderProductsEntity, Long> {
    /**
     * Retrieves all products from join table by user ID.
     * @param userID The ID of the user.
     * @return A list of {@link OrderProductsEntity} objects containing all orders and products related to the user.
     */
    @Query(value = "SELECT * FROM OrderProducts WHERE OrderID IN " +
            "(SELECT OrderID FROM Orders WHERE User_ID = ?1)", nativeQuery = true)
    List<OrderProductsEntity> findOrdersByUserID(int userID);

}
