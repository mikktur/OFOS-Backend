package ofos.repository;

import ofos.entity.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductRepository extends JpaRepository<ProductEntity, Long> {

    ProductEntity findByProductId(int id);


    @Modifying
    @Query("UPDATE ProductEntity p set p.active = false WHERE p.productId = ?1")
    void updateAvailability(int id);

    @Query("SELECT p FROM ProductEntity p JOIN p.restaurants r WHERE r.restaurantID = :restaurantID")
    List<ProductEntity> getProductsByRestaurant(@Param("restaurantID") Integer restaurantID);

    @Modifying
    @Query(value = "INSERT INTO Provides (RestaurantID, ProductID)" +
            "VALUES (?1, ?2)",
            nativeQuery = true)
    void addProductToRestaurant(int restaurantID, int productID);

    @Query(value = "SELECT ProductID from Products WHERE ProductName = ?1",
            nativeQuery = true)
    int findIdByName(String name);



}
