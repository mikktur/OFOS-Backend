package service;

import ofos.dto.RestaurantDTO;
import ofos.dto.UpdateRestaurantDTO;
import ofos.entity.RestaurantEntity;
import ofos.entity.UserEntity;
import ofos.repository.RestaurantRepository;
import ofos.repository.UserRepository;
import ofos.service.RestaurantService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
public class RestaurantServiceTests {

    @Mock
    private RestaurantRepository restaurantRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private RestaurantService restaurantService;

    private UserEntity createUser(int id, String username) {
        UserEntity user = new UserEntity();
        user.setUserId(id);
        user.setUsername(username);
        return user;
    }

    private RestaurantEntity createRestaurant(int id, String name, String phone, String picture, UserEntity owner) {
        RestaurantEntity restaurant = new RestaurantEntity();
        restaurant.setId(id);
        restaurant.setRestaurantName(name);
        restaurant.setRestaurantPhone(phone);
        restaurant.setPicture(picture);
        restaurant.setOwner(owner);
        return restaurant;
    }

    @Test
    public void getAllRestaurantsTest() {
        // Arrange: Create users and restaurants
        UserEntity owner1 = createUser(1, "Owner1");
        UserEntity owner2 = createUser(2, "Owner2");

        RestaurantEntity restaurant1 = createRestaurant(1, "McDonalds", "123456789", "mcdonalds.jpg", owner1);
        RestaurantEntity restaurant2 = createRestaurant(2, "Burger King", "987654321", "burgerking.jpg", owner2);

        List<RestaurantEntity> restaurants = List.of(restaurant1, restaurant2);

        // Mock repository response
        Mockito.when(restaurantRepository.findAll()).thenReturn(restaurants);

        // Act
        List<RestaurantDTO> returnedRestaurants = restaurantService.getAllRestaurants();

        // Assert
        Assertions.assertEquals(2, returnedRestaurants.size());
        Assertions.assertEquals("McDonalds", returnedRestaurants.get(0).getRestaurantName());
        Assertions.assertEquals("Burger King", returnedRestaurants.get(1).getRestaurantName());
        Assertions.assertEquals("Owner1", returnedRestaurants.get(0).getOwnerUsername());
        Assertions.assertEquals("Owner2", returnedRestaurants.get(1).getOwnerUsername());
    }

    @Test
    public void getRestaurantsByOwnerTest() {
        // Arrange: Create owner and restaurants
        UserEntity owner = createUser(30, "Owner30");
        owner.setRole("Owner");

        RestaurantEntity restaurant1 = createRestaurant(1, "McDonalds", "123456789", "mcdonalds.jpg", owner);
        RestaurantEntity restaurant2 = createRestaurant(2, "Burger King", "987654321", "burgerking.jpg", owner);

        List<RestaurantEntity> restaurants = List.of(restaurant1, restaurant2);

        // Mock repository behavior
        Mockito.when(userRepository.findById(30L)).thenReturn(Optional.of(owner));
        Mockito.when(restaurantRepository.findByOwner_UserId(30L)).thenReturn(restaurants);

        // Act
        List<RestaurantDTO> returnedRestaurants = restaurantService.getRestaurantsByOwner(30L);

        // Assert
        Assertions.assertEquals(2, returnedRestaurants.size());
        Assertions.assertEquals("McDonalds", returnedRestaurants.get(0).getRestaurantName());
        Assertions.assertEquals("Burger King", returnedRestaurants.get(1).getRestaurantName());
        Assertions.assertEquals("Owner30", returnedRestaurants.get(0).getOwnerUsername());
        Assertions.assertEquals("Owner30", returnedRestaurants.get(1).getOwnerUsername());
    }

    @Test
    public void getRestaurantsByCategoryTest() {
        // Arrange: Create users and restaurants
        UserEntity owner1 = createUser(1, "Owner1");
        UserEntity owner2 = createUser(2, "Owner2");

        RestaurantEntity restaurant1 = createRestaurant(1, "McDonalds", "123456789", "mcdonalds.jpg", owner1);
        RestaurantEntity restaurant2 = createRestaurant(2, "Burger King", "987654321", "burgerking.jpg", owner2);

        List<RestaurantEntity> restaurants = List.of(restaurant1, restaurant2);

        // Mock repository behavior
        Mockito.when(restaurantRepository.findByCategoryName("hamburgers")).thenReturn(restaurants);

        // Act
        List<RestaurantDTO> returnedRestaurants = restaurantService.getRestaurantsByCategory("hamburgers");

        // Assert
        Assertions.assertEquals(2, returnedRestaurants.size());
        Assertions.assertEquals("McDonalds", returnedRestaurants.get(0).getRestaurantName());
        Assertions.assertEquals("Burger King", returnedRestaurants.get(1).getRestaurantName());
        Assertions.assertEquals("Owner1", returnedRestaurants.get(0).getOwnerUsername());
        Assertions.assertEquals("Owner2", returnedRestaurants.get(1).getOwnerUsername());
    }

    @Test
    public void updateRestaurantTest() {
        // Arrange: Create user and restaurant
        UserEntity owner1 = createUser(1, "Owner1");
        RestaurantEntity restaurant = createRestaurant(1, "Old Name", "123456789", "oldpicture.jpg", owner1);

        // Mock repository behavior
        Mockito.when(restaurantRepository.findById(1)).thenReturn(Optional.of(restaurant));
        Mockito.when(restaurantRepository.save(any(RestaurantEntity.class))).thenReturn(restaurant);

        // Act: Call the service method to update the restaurant
        UpdateRestaurantDTO updateRequest = new UpdateRestaurantDTO();
        updateRequest.setRestaurantName("New Name");
        updateRequest.setRestaurantPhone("987654321");
        updateRequest.setPicture("newpicture.jpg");

        RestaurantDTO updatedRestaurant = restaurantService.updateRestaurant(1, updateRequest);

        // Assert
        Assertions.assertEquals("New Name", updatedRestaurant.getRestaurantName());
        Assertions.assertEquals("987654321", updatedRestaurant.getRestaurantPhone());
        Assertions.assertEquals("newpicture.jpg", updatedRestaurant.getPicture());
    }
}
