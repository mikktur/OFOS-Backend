package ofos.service;

import ofos.dto.ContactInfoDTO;
import ofos.entity.ContactInfoEntity;
import ofos.entity.UserEntity;
import ofos.repository.ContactInfoRepository;
import ofos.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;


@Service
public class ContactInfoService {

    @Autowired
    ContactInfoRepository contactInfoRepository;
    @Autowired
    UserRepository userRepository;

    /**
     * Retrieves contact info for a user.
     * @param userID The ID of the user.
     * @return A {@link ContactInfoEntity} object representing the contact info in the database.
     */
    public ContactInfoEntity getContactInfo(int userID){
        return contactInfoRepository.findContactInfoEntityByUserId(userID);
    }

    /**
     * Updates contact info for a user.
     * @param contactInfoDTO The contact info to be updated.
     * @param username The username of the user.
     * @return {@link ResponseEntity} object with a message and a status code.
     */
    public ResponseEntity<String> updateContactInfo(ContactInfoDTO contactInfoDTO, String username){
        UserEntity user = userRepository.findByUsername(username);
        if (user.getUserId() == contactInfoDTO.getUserId()){
            ContactInfoEntity contactInfoEntity = new ContactInfoEntity();
            contactInfoRepository.save(createEntity(contactInfoDTO, contactInfoEntity));
            return new ResponseEntity<>(
                    "Contact info updated.",
                    HttpStatus.OK
            );
        }
        return new ResponseEntity<>(
                "Something went wrong.",
                HttpStatus.BAD_REQUEST
        );
    }

    /**
     * Saves contact info for a user.
     * @param dto The contact info to be saved.
     * @param username The username of the user.
     * @return {@link ResponseEntity} object with a message and a status code.
     */
    public ResponseEntity<String> saveContactInfo(ContactInfoDTO dto, String username) {
        try {
            UserEntity user = userRepository.findByUsername(username);
            if (user == null) {
                throw new Exception("User not found for username: " + username);
            }

            ContactInfoEntity contactInfo = new ContactInfoEntity();
            contactInfo.setFirstName(dto.getFirstName());
            contactInfo.setLastName(dto.getLastName());
            contactInfo.setEmail(dto.getEmail());
            contactInfo.setPhoneNumber(dto.getPhoneNumber());
            contactInfo.setAddress(dto.getAddress());
            contactInfo.setCity(dto.getCity());
            contactInfo.setPostalCode(dto.getPostalCode());
            contactInfo.setUserId(user.getUserId());

            contactInfoRepository.save(contactInfo);
            return new ResponseEntity<>(
                    "Contact info saved for: " + contactInfo.getFirstName(),
                    HttpStatus.OK
            );
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(
                    "Something went wrong.",
                    HttpStatus.BAD_REQUEST
            );
        }
    }


    /**
     * Creates a {@link ContactInfoEntity} object from a {@link ContactInfoDTO} object.
     * @param contactInfoDTO The {@link ContactInfoDTO} object.
     * @param contactInfoEntity The {@link ContactInfoEntity} object.
     * @return The {@link ContactInfoEntity} object.
     */
    protected ContactInfoEntity createEntity(ContactInfoDTO contactInfoDTO, ContactInfoEntity contactInfoEntity){
        contactInfoEntity.setPhoneNumber(contactInfoDTO.getPhoneNumber());
        contactInfoEntity.setCity(contactInfoDTO.getCity());
        contactInfoEntity.setAddress(contactInfoDTO.getAddress());
        contactInfoEntity.setFirstName(contactInfoDTO.getFirstName());
        contactInfoEntity.setEmail(contactInfoDTO.getEmail());
        contactInfoEntity.setLastName(contactInfoDTO.getLastName());
        contactInfoEntity.setUserId(contactInfoDTO.getUserId());
        contactInfoEntity.setPostalCode(contactInfoDTO.getPostalCode());
        return contactInfoEntity;
    }


}
