package ofos.repository;

import ofos.entity.UsersAddressEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsersAddressRepository extends JpaRepository<UsersAddressEntity, Long> {

    UsersAddressEntity getUsersAddressEntityByDeliveryAddressId(int id);

}
