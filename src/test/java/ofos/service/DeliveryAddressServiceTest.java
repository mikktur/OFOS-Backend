package ofos.service;

import ofos.dto.DeliveryAddressDTO;
import ofos.entity.DeliveryAddressEntity;
import ofos.entity.UserEntity;
import ofos.entity.UsersAddressEntity;
import ofos.repository.DeliveryAddressRepository;
import ofos.repository.UserRepository;
import ofos.repository.UsersAddressRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class DeliveryAddressServiceTest {

    @InjectMocks
    DeliveryAddressService deliveryAddressService;

    @Mock
    DeliveryAddressRepository deliveryAddressRepository;

    @Mock
    UserRepository userRepository;

    @Mock
    UsersAddressRepository usersAddressRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getDeliveryAddressesTest() {
        List<DeliveryAddressEntity> addressEntities = new ArrayList<>();
        DeliveryAddressEntity deliveryAddressEntity = new DeliveryAddressEntity();
        deliveryAddressEntity.setStreetAddress("Osoite 1");
        deliveryAddressEntity.setCity("Kaupunki");
        deliveryAddressEntity.setInfo("Ovikoodi: 777");
        deliveryAddressEntity.setPostalCode("00220");
        addressEntities.add(deliveryAddressEntity);

        when(deliveryAddressRepository.getAddressesById(anyInt())).thenReturn(addressEntities);
        List<DeliveryAddressEntity> result = deliveryAddressService.getDeliveryAddresses(1);


        assertEquals(1, result.size());
    }

    @Test
    void getDeliveryAddressesWithDefaultFlagTest() {
        List<UsersAddressEntity> usersAddressEntities = new ArrayList<>();
        UsersAddressEntity usersAddressEntity = new UsersAddressEntity();
        usersAddressEntity.setUserId(1);
        usersAddressEntity.setDeliveryAddressId(1);
        usersAddressEntity.setIsDefault(true);
        usersAddressEntities.add(usersAddressEntity);
        DeliveryAddressEntity deliveryAddressEntity = new DeliveryAddressEntity();
        deliveryAddressEntity.setStreetAddress("Osoite 1");
        deliveryAddressEntity.setCity("Kaupunki");
        deliveryAddressEntity.setInfo("Ovikoodi: 777");
        deliveryAddressEntity.setPostalCode("00220");


        when(usersAddressRepository.findByUserId(anyInt())).thenReturn(usersAddressEntities);
        when(deliveryAddressRepository.findById(anyInt())).thenReturn(Optional.of(deliveryAddressEntity));
        List<DeliveryAddressDTO> result = deliveryAddressService.getDeliveryAddressesWithDefaultFlag(1);
        assertEquals(1, result.size());
    }

    @Test
    void saveDeliveryAddressTest() {
        UserEntity user = new UserEntity(1, "Makko", "Jaronen", "User", false);
        DeliveryAddressEntity deliveryAddressEntity = new DeliveryAddressEntity();
        deliveryAddressEntity.setStreetAddress("Osoite 1");
        deliveryAddressEntity.setCity("Kaupunki");
        deliveryAddressEntity.setInfo("Ovikoodi: 777");
        deliveryAddressEntity.setPostalCode("00220");
        DeliveryAddressDTO deliveryAddressDTO = new DeliveryAddressDTO(
                "Osoite 1",
                "Kaupunki",
                "00220",
                1,
                "Ovikoodi: 777",
                true
        );


        when(userRepository.findByUsername(anyString())).thenReturn(user);
        when(usersAddressRepository.existsByUserId(anyInt())).thenReturn(true);
        when(deliveryAddressRepository.save(any())).thenReturn(deliveryAddressEntity);

        ResponseEntity<String> result = deliveryAddressService.saveDeliveryAddress(deliveryAddressDTO, "Makko");

        assertEquals("Saved successfully.", result.getBody());
        assertEquals(HttpStatusCode.valueOf(200), result.getStatusCode());
    }

    @Test
    void setDefaultDeliveryAddressTest() {
        UserEntity user = new UserEntity(1, "Makko", "Jaronen", "User", false);
        UsersAddressEntity usersAddressEntity = new UsersAddressEntity();
        usersAddressEntity.setUserId(1);
        usersAddressEntity.setDeliveryAddressId(1);
        usersAddressEntity.setIsDefault(true);

        when(userRepository.findByUsername(anyString())).thenReturn(user);
        when(usersAddressRepository.findByUserIdAndDeliveryAddressId(anyInt(), anyInt())).thenReturn(usersAddressEntity);
        ResponseEntity<String> result = deliveryAddressService.setDefaultDeliveryAddress(1, 1, "Makko");
        assertEquals(HttpStatusCode.valueOf(200), result.getStatusCode());
        assertEquals("Default address set successfully.", result.getBody());
    }

    @Test
    void updateDeliveryAddressTest() {
        when(deliveryAddressRepository.getByDeliveryAddressId(anyInt())).thenReturn(new DeliveryAddressEntity());
        when(deliveryAddressRepository.save(any())).thenReturn(new DeliveryAddressEntity());
        ResponseEntity<String> result = deliveryAddressService.updateDeliveryAddress(new DeliveryAddressDTO());
        assertEquals(HttpStatusCode.valueOf(200), result.getStatusCode());
        assertEquals("Updated successfully.", result.getBody());
    }

    @Test
    void deleteDeliveryAddressTest() {
        UserEntity user = new UserEntity(1, "Makko", "Jaronen", "User", false);
        UsersAddressEntity usersAddressEntity = new UsersAddressEntity();
        usersAddressEntity.setUserId(1);
        usersAddressEntity.setDeliveryAddressId(1);
        usersAddressEntity.setIsDefault(true);

        when(userRepository.findByUsername(anyString())).thenReturn(user);
        when(usersAddressRepository.getUsersAddressEntityByDeliveryAddressId(anyInt())).thenReturn(usersAddressEntity);
        ResponseEntity<String> result = deliveryAddressService.deleteDeliveryAddress(1, "username");
        assertEquals(HttpStatusCode.valueOf(200), result.getStatusCode());
        assertEquals("Deleted successfully.", result.getBody());
    }
}