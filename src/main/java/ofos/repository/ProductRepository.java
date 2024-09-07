package ofos.repository;

import ofos.entity.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProductRepository extends JpaRepository<ProductEntity, Long> {

    ProductEntity findByProductId(int id);

    @Modifying
    @Query("UPDATE ProductEntity p set p.active = false WHERE p.productId = ?1")
    void updateAvailability(int id);

    @Query(value = "SELECT pr.* FROM Products pr " +
            "INNER JOIN Provides prov ON pr.ProductID = prov.ProductID " +
            "INNER JOIN Restaurants r ON r.RestaurantID = prov.RestaurantID " +
            "WHERE r.restaurantName = ?1",
            nativeQuery = true)
    List<ProductEntity> getProductsByRestaurant(String restaurant);
    
}
