package ofos.repository;

import ofos.entity.ProductEntity;
import ofos.entity.TranslationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TranslationRepository extends JpaRepository<TranslationEntity, Integer> {

    @Query("""
    SELECT t
    FROM TranslationEntity t
    WHERE t.product = :product AND t.id.lang = :lang
""")
    TranslationEntity findByProductAndLang(@Param("product") ProductEntity product, @Param("lang") String lang);

}
