package ofos.repository;

import ofos.entity.DeliveryAddressEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface DeliveryAddressRepository extends JpaRepository<DeliveryAddressEntity, Long> {


    @Query(value = "SELECT * FROM DeliveryAddresses WHERE DeliveryAddressID IN " +
            "(SELECT DeliveryAddressID FROM UsersAddress WHERE User_ID = ?1)",
            nativeQuery = true)
    List<DeliveryAddressEntity> getAddressesById(int user_ID);

    @Modifying
    long deleteByDeliveryAddressId(int addressID);

    @Modifying
    @Query(value = "DELETE FROM UsersAddress WHERE DeliveryAddressID = ?1", nativeQuery = true)
    void deleteUsersAddressByDeliveryAddressId(int deliveryAddressId);

    DeliveryAddressEntity getByDeliveryAddressId(int id);


}
