package ofos.service;

import ofos.dto.ContactInfoDTO;
import ofos.entity.ContactInfoEntity;
import ofos.entity.UserEntity;
import ofos.repository.ContactInfoRepository;
import ofos.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

class ContactInfoServiceTest {

    @Mock
    private ContactInfoRepository contactInfoRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private ContactInfoService contactInfoService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getContactInfoTest() {
        int userId = 1;
        ContactInfoEntity contactInfoEntity = new ContactInfoEntity();
        contactInfoEntity.setPhoneNumber("0502222222");
        contactInfoEntity.setAddress("Osoite 1");
        contactInfoEntity.setCity("Kaupunki");
        contactInfoEntity.setFirstName("Matti");
        contactInfoEntity.setLastName("Meikäläinen");
        contactInfoEntity.setPostalCode("00220");
        contactInfoEntity.setUserId(1);


        when(contactInfoRepository.findContactInfoEntityByUserId(userId)).thenReturn(contactInfoEntity);

        ContactInfoEntity result = contactInfoService.getContactInfo(userId);

        assertEquals(contactInfoEntity, result);
    }

    @Test
    void updateContactInfoTest() {
        String username = "test";
        ContactInfoDTO contactInfoDTO = new ContactInfoDTO(
                "050222222",
                     "Osoite 1",
                "Kaupunki",
                "Matti",
                "Meikäläinen",
                "email@gmail.com",
                "00220",
                1
                );
        UserEntity userEntity = new UserEntity();
        userEntity.setUserId(1);
        when(userRepository.findByUsername(username)).thenReturn(userEntity);

        ResponseEntity<String> result = contactInfoService.updateContactInfo(contactInfoDTO, username);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals("Contact info updated.", result.getBody());
        System.out.println(result.getBody());
    }

    @Test
    void saveContactInfoTest() {
        String username = "test";
        ContactInfoDTO contactInfoDTO = new ContactInfoDTO(
                "050222222",
                "Osoite 1",
                "Kaupunki",
                "Matti",
                "Meikäläinen",
                "email@gmail.com",
                "00220",
                1
        );
        UserEntity userEntity = new UserEntity();
        userEntity.setUserId(1);
        when(userRepository.findByUsername(username)).thenReturn(userEntity);

        ResponseEntity<String> result = contactInfoService.saveContactInfo(contactInfoDTO, username);

        assertEquals(HttpStatus.OK, result.getStatusCode());
    }

    @Test
    void createEntityTest() {
        ContactInfoDTO contactInfoDTO = new ContactInfoDTO(
                "050222222",
                "Osoite 1",
                "Kaupunki",
                "Matti",
                "Meikäläinen",
                "email@gmail.com",
                "00220",
                1
        );
        ContactInfoEntity contactInfoEntity = new ContactInfoEntity();

        ContactInfoEntity result = contactInfoService.createEntity(contactInfoDTO, contactInfoEntity);


        assertEquals(contactInfoEntity, result);
    }
}