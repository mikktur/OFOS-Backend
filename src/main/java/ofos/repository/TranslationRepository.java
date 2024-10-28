package ofos.repository;

import ofos.entity.TranslationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TranslationRepository extends JpaRepository<TranslationEntity, Long> {

    TranslationEntity findByProductIdAndLang(int id, String lang);

    @Query("SELECT t FROM TranslationEntity t WHERE t.productId IN (SELECT p.ProductID FROM ProvidesEntity p WHERE p.RestaurantID = ?1)" +
            "AND t.lang = ?2")
    List<TranslationEntity> findTranslationEntitiesByProductIdAndLang(int id, String lang);

}
