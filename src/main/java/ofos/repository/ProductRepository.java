package ofos.repository;

import ofos.entity.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


import java.util.List;

/**
 * Repository for the {@link ProductEntity} class.
 */

public interface ProductRepository extends JpaRepository<ProductEntity, Long> {

    ProductEntity findByProductId(int id);

    /**
     * Updates the availability of a product.
     * @param id The ID of the product.
     */
    @Modifying
    @Query("UPDATE ProductEntity p set p.active = false WHERE p.productId = ?1")
    void updateAvailability(int id);

    /**
     * Retrieves all products by restaurant ID.
     * @param restaurantID The ID of the restaurant.
     * @return A list of {@link ProductEntity} objects containing all products related to the restaurant.
     */
    @Query("SELECT p FROM ProductEntity p JOIN p.restaurants r WHERE r.restaurantID = :restaurantID")
    List<ProductEntity> getProductsByRestaurant(@Param("restaurantID") Integer restaurantID);

    /**
     * Adds a product to a restaurant.
     * @param restaurantID The ID of the restaurant.
     * @param productID The ID of the product.
     */
    @Query(value = "INSERT INTO Provides (RestaurantID, ProductID)" +
            "VALUES (?1, ?2)",
            nativeQuery = true)
    void addProductToRestaurant(int restaurantID, int productID);

    /**
     * Retrieves all products by name.
     * @param name The nameof the product.
     * @return A list of {@link ProductEntity} objects containing all products with the name.
     */
    @Query(value = "SELECT ProductID from Products WHERE ProductName = ?1",
            nativeQuery = true)
    int findIdByName(String name);



}
