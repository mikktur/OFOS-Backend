package ofos.repository;

import ofos.entity.UsersAddressEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface UsersAddressRepository extends JpaRepository<UsersAddressEntity, Integer> {
    boolean existsByUserId(int userId);

    boolean existsByUserIdAndIsDefaultTrue(int userId);


    @Modifying
    @Query("UPDATE UsersAddressEntity ua SET ua.isDefault = NULL WHERE ua.userId = :userId")
    void unsetDefaultAddresses(@Param("userId") int userId);

    UsersAddressEntity findByUserIdAndDeliveryAddressId(int userId, int deliveryAddressId);


    UsersAddressEntity findFirstByUserIdOrderByDeliveryAddressIdAsc(int userId);

    // Existing methods
    List<UsersAddressEntity> findByUserId(int userId);

    UsersAddressEntity getUsersAddressEntityByDeliveryAddressId(int deliveryAddressId);
}
