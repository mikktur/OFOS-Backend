package ofos.repository;

import ofos.entity.ContactInfoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

public interface ContactInfoRepository extends JpaRepository<ContactInfoEntity, Long> {

    ContactInfoEntity findContactInfoEntityByUserId(int userID);

//    void updateContactInfoEntityByUserId(int usedID);

}
