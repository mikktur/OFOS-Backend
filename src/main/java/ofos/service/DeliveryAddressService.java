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

import java.util.ArrayList;
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

    public List<DeliveryAddressDTO> getDeliveryAddressesWithDefaultFlag(int userId){
        List<UsersAddressEntity> usersAddresses = usersAddressRepository.findByUserId(userId);

        List<DeliveryAddressDTO> addressDTOs = new ArrayList<>();
        for (UsersAddressEntity ua : usersAddresses) {
            DeliveryAddressEntity addressEntity = deliveryAddressRepository.findById(ua.getDeliveryAddressId()).orElse(null);
            if (addressEntity != null) {
                DeliveryAddressDTO dto = new DeliveryAddressDTO();
                dto.setDeliveryAddressId(addressEntity.getDeliveryAddressId());
                dto.setStreetAddress(addressEntity.getStreetAddress());
                dto.setCity(addressEntity.getCity());
                dto.setPostalCode(addressEntity.getPostalCode());
                dto.setInfo(addressEntity.getInfo());
                dto.setDefaultAddress(Boolean.TRUE.equals(ua.getIsDefault()));
                addressDTOs.add(dto);
            }
        }
        return addressDTOs;
    }


    @Transactional
    public boolean saveDeliveryAddress(DeliveryAddressDTO deliveryAddressDTO, String username){
        try {
            int userID = userRepository.findByUsername(username).getUserId();

            // Check if the user has any existing delivery addresses
            boolean hasAddresses = usersAddressRepository.existsByUserId(userID);

            // Create and save the DeliveryAddressEntity
            DeliveryAddressEntity dae = new DeliveryAddressEntity();
            dae = deliveryAddressRepository.save(setValues(deliveryAddressDTO, dae));

            // Create and save the UsersAddressEntity
            UsersAddressEntity uae = new UsersAddressEntity();
            uae.setDeliveryAddressId(dae.getDeliveryAddressId());
            uae.setUserId(userID);

            if (!hasAddresses) {
                // Set as default since the user has no other addresses
                uae.setIsDefault(true);
            } else {
                // Not default
                uae.setIsDefault(null); // or set to false
            }

            usersAddressRepository.save(uae);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Transactional
    public boolean setDefaultDeliveryAddress(int deliveryAddressId, int userId, String username) {
        try {
            // Verify that the userId matches the username
            int authenticatedUserId = userRepository.findByUsername(username).getUserId();
            if (authenticatedUserId != userId) {
                System.out.println("User ID does not match the authenticated user");
                return false; // User ID does not match the authenticated user
            }

            // Unset the current default address
            usersAddressRepository.unsetDefaultAddresses(userId);

            // Set the new default address
            UsersAddressEntity usersAddressEntity = usersAddressRepository.findByUserIdAndDeliveryAddressId(userId, deliveryAddressId);
            if (usersAddressEntity != null) {
                usersAddressEntity.setIsDefault(true);
                usersAddressRepository.save(usersAddressEntity);
                return true;
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    public boolean updateDeliveryAddress(DeliveryAddressDTO deliveryAddressDTO) {
        DeliveryAddressEntity addresses = deliveryAddressRepository.getByDeliveryAddressId(deliveryAddressDTO.getDeliveryAddressId());
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

            // Check if the deleted address was the default
            boolean hasDefaultAddress = usersAddressRepository.existsByUserIdAndIsDefaultTrue(userID);
            if (!hasDefaultAddress) {
                // If the user has other addresses, set one as the default
                UsersAddressEntity newDefault = usersAddressRepository.findFirstByUserIdOrderByDeliveryAddressIdAsc(userID);
                if (newDefault != null) {
                    newDefault.setIsDefault(true);
                    usersAddressRepository.save(newDefault);
                }
            }

            return true;
        }
        return false;
    }

    private DeliveryAddressEntity setValues(DeliveryAddressDTO deliveryAddressDTO, DeliveryAddressEntity deliveryAddressEntity){
        deliveryAddressEntity.setDeliveryAddressId(deliveryAddressDTO.getDeliveryAddressId());
        deliveryAddressEntity.setStreetAddress(deliveryAddressDTO.getStreetAddress());
        deliveryAddressEntity.setCity(deliveryAddressDTO.getCity());
        deliveryAddressEntity.setPostalCode(deliveryAddressDTO.getPostalCode());
        deliveryAddressEntity.setInfo(deliveryAddressDTO.getInfo());
        return deliveryAddressEntity;
    }

}
