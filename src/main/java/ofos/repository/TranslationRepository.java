package ofos.repository;

import ofos.entity.TranslationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TranslationRepository extends JpaRepository<TranslationEntity, Long> {

    TranslationEntity findByProductIdAndLang(int id, String lang);

    List<TranslationEntity> findTranslationEntitiesByProductIdAndLang(int id, String lang);

}
