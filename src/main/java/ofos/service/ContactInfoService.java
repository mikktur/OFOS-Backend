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
import org.springframework.transaction.annotation.Transactional;


@Service
public class ContactInfoService {

    @Autowired
    ContactInfoRepository contactInfoRepository;
    @Autowired
    UserRepository userRepository;


    public ContactInfoEntity getContactInfo(int userID){
        return contactInfoRepository.findContactInfoEntityByUserId(userID);
    }


    @Transactional
    public ResponseEntity<String> updateContactInfo(ContactInfoDTO contactInfoDTO, String username){
        UserEntity user = userRepository.findByUsername(username);
        if (user.getUserId().equals(contactInfoDTO.getUserId())){
            ContactInfoEntity contactInfoEntity = contactInfoRepository.findContactInfoEntityByUserId(user.getUserId());
            if (contactInfoEntity == null){
                return new ResponseEntity<>(
                        "Contact info not found.",
                        HttpStatus.NOT_FOUND
                );
            }
            contactInfoEntity.setAddress(contactInfoDTO.getAddress());
            contactInfoEntity.setCity(contactInfoDTO.getCity());
            contactInfoEntity.setEmail(contactInfoDTO.getEmail());
            contactInfoEntity.setFirstName(contactInfoDTO.getFirstName());
            contactInfoEntity.setLastName(contactInfoDTO.getLastName());
            contactInfoEntity.setPhoneNumber(contactInfoDTO.getPhoneNumber());
            contactInfoEntity.setPostalCode(contactInfoDTO.getPostalCode());
            contactInfoRepository.save(contactInfoEntity);

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





}
