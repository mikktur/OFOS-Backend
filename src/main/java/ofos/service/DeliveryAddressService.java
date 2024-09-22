package ofos.service;

import ofos.dto.DeliveryAddressDTO;
import ofos.entity.DeliveryAddressEntity;
import ofos.entity.UsersAddressEntity;
import ofos.repository.DeliveryAddressRepository;
import ofos.repository.UserRepository;
import ofos.repository.UsersAddressRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
public class DeliveryAddressService {

    @Autowired
    DeliveryAddressRepository deliveryAddressRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    UsersAddressRepository usersAddressRepository;

    public List<DeliveryAddressEntity> getDeliveryAddresses(int id){
        return deliveryAddressRepository.getAddressesById(id);
    }

    public boolean saveDeliveryAddress(DeliveryAddressDTO deliveryAddressDTO, String username){
        try {
            int userID = userRepository.findByUsername(username).getUserId();
            DeliveryAddressEntity dae = new DeliveryAddressEntity();
            dae = deliveryAddressRepository.save(setValues(deliveryAddressDTO, dae));
            UsersAddressEntity uae = new UsersAddressEntity();
            uae.setDeliveryAddressId(dae.getDeliveryAddressId());
            uae.setUserId(userID);
            usersAddressRepository.save(uae);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean updateDeliveryAddress(DeliveryAddressDTO deliveryAddressDTO){
       DeliveryAddressEntity addresses = deliveryAddressRepository.getByDeliveryAddressId(deliveryAddressDTO.getDeliveryAddressID());
        if (addresses != null) {
            DeliveryAddressEntity dae = new DeliveryAddressEntity();
            deliveryAddressRepository.save(setValues(deliveryAddressDTO, dae));
            return true;
        }
        return false;
    }

    @Transactional
    public boolean deleteDeliveryAddress(int addressID, String username){
        int userID = userRepository.findByUsername(username).getUserId();
        int addressConfirmationID = usersAddressRepository.getUsersAddressEntityByDeliveryAddressId(addressID).getUserId();
        if (userID == addressConfirmationID) {
            deliveryAddressRepository.deleteUsersAddressByDeliveryAddressId(addressID);
            deliveryAddressRepository.deleteByDeliveryAddressId(addressID);
            return true;
        }
        return false;
    }



    private DeliveryAddressEntity setValues(DeliveryAddressDTO deliveryAddressDTO, DeliveryAddressEntity deliveryAddressEntity){
        deliveryAddressEntity.setDeliveryAddressId(deliveryAddressDTO.getDeliveryAddressID());
        deliveryAddressEntity.setStreetAddress(deliveryAddressDTO.getStreetAddress());
        deliveryAddressEntity.setCity(deliveryAddressDTO.getCity());
        deliveryAddressEntity.setPostalCode(deliveryAddressDTO.getPostCode());
        return deliveryAddressEntity;
    }

}
