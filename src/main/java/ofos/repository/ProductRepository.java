package ofos.repository;

import ofos.entity.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


import java.util.List;
import java.util.Optional;



public interface ProductRepository extends JpaRepository<ProductEntity, Integer> {
    @Query("SELECT p FROM ProductEntity p LEFT JOIN FETCH p.translations WHERE p.productId = :productId")
    Optional<ProductEntity> findByProductId(@Param("productId") int productId);

    @Modifying
    @Query("UPDATE ProductEntity p set p.active = false WHERE p.productId = ?1")
    void updateAvailability(int id);


    @Query("SELECT p FROM ProductEntity p JOIN p.restaurants r WHERE p.productId = :productId AND r.restaurantID = :restaurantId")
    Optional<ProductEntity> findByProductIdAndRestaurantId(@Param("productId") int productId, @Param("restaurantId") int restaurantId);



    @Query("""
    SELECT p, t 
    FROM ProductEntity p
    LEFT JOIN TranslationEntity t ON p.productId = t.product.productId AND t.id.lang = :lang
    JOIN p.restaurants r
    WHERE r.restaurantID = :restaurantId
""")
    List<Object[]> findProductsWithTranslations(@Param("restaurantId") int restaurantId, @Param("lang") String lang);


}
