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
