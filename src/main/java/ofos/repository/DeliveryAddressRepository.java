package ofos.repository;

import ofos.entity.DeliveryAddressEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Repository for the {@link DeliveryAddressEntity} class.
 */
public interface DeliveryAddressRepository extends JpaRepository<DeliveryAddressEntity, Integer> {

    /**
     * Retrieves all delivery addresses by user ID.
     * @param user_ID The ID of the user.
     * @return A list of {@link DeliveryAddressEntity} objects containing all addresses related to the user.
     */
    @Query(value = "SELECT * FROM DeliveryAddresses WHERE DeliveryAddressID IN " +
            "(SELECT DeliveryAddressID FROM UsersAddress WHERE User_ID = ?1)",
            nativeQuery = true)
    List<DeliveryAddressEntity> getAddressesById(int user_ID);

    /**
     * Deletes a delivery address by its ID.
     * @param addressID The ID of the address.
     * @return The number of deleted addresses.
     */
    @Modifying
    int deleteByDeliveryAddressId(int addressID);

    /**
     * Deletes a user's address from the jointable by the delivery address ID.
     * @param deliveryAddressId The ID of the delivery address.
     */
    @Modifying
    @Query(value = "DELETE FROM UsersAddress WHERE DeliveryAddressID = ?1", nativeQuery = true)
    void deleteUsersAddressByDeliveryAddressId(int deliveryAddressId);

    DeliveryAddressEntity getByDeliveryAddressId(int id);


}
