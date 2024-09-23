package ofos.service;

import ofos.dto.ContactInfoDTO;
import ofos.entity.ContactInfoEntity;
import ofos.entity.UserEntity;
import ofos.repository.ContactInfoRepository;
import ofos.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class ContactInfoService {

    @Autowired
    ContactInfoRepository contactInfoRepository;
    @Autowired
    UserRepository userRepository;


    public ContactInfoEntity getContactInfo(int userID){
        return contactInfoRepository.findContactInfoEntityByUserId(userID);
    }

    public boolean updateContactInfo(ContactInfoDTO contactInfoDTO, String username){
        UserEntity user = userRepository.findByUsername(username);
        if (user.getUserId() == contactInfoDTO.getUserId()){
            ContactInfoEntity contactInfoEntity = new ContactInfoEntity();
            contactInfoRepository.save(createEntity(contactInfoDTO, contactInfoEntity));
            return true;
        }
        return false;
    }

    public boolean saveContactInfo(ContactInfoDTO dto, String username) {
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
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }



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
