package ofos.service;

import jakarta.transaction.Transactional;
import ofos.dto.RestaurantDTO;
import ofos.dto.UpdateRestaurantDTO;
import ofos.entity.RestaurantEntity;
import ofos.entity.UserEntity;
import ofos.exception.UserNotFoundException;
import ofos.exception.UserNotOwnerException;
import ofos.repository.RestaurantRepository;
import ofos.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

/**
 * This class provides methods to interact and save the restaurant data stored in the database.
 */
@Service
public class RestaurantService {

    private final RestaurantRepository restaurantRepository;
    private final UserRepository userRepository;

    @Autowired
    public RestaurantService(RestaurantRepository restaurantRepository, UserRepository userRepository) {
        this.restaurantRepository = restaurantRepository;
        this.userRepository = userRepository;
    }

    /**
     * Retrieves all restaurants from the database.
     *
     * @return A list of {@link RestaurantDTO} objects representing all restaurants in the database.
     */
    public List<RestaurantDTO> getAllRestaurants() {
        System.out.println("Entered getAllRestaurants method in RestaurantService");
        List<RestaurantEntity> restaurants = restaurantRepository.findAll();
        // Print all restaurants retrieved from the repository
        System.out.println("Restaurants retrieved: ");
        restaurants.forEach(restaurant ->
                System.out.println("Restaurant Name: " + restaurant.getRestaurantName() + ", Owner: " + restaurant.getOwner().getUsername())
        );

        return restaurants.stream()
                .map(RestaurantDTO::fromEntity)
                .collect(Collectors.toList());
    }

    /**
     * Retrieves all restaurants owned by a user from the database.
     *
     * @param userId The ID of the user.
     * @return A list of {@link RestaurantDTO} objects representing all restaurants owned by the user in the database.
     */
    public List<RestaurantDTO> getRestaurantsByOwner(long userId) {
        System.out.println("Entered getRestaurantsByOwner method in RestaurantService");
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        System.out.println("User retrieved: " + user.getUsername() + " role: " + user.getRole());
        if (!"OWNER".equals(user.getRole().toUpperCase())) {
            throw new UserNotOwnerException("User is not an owner");
        }

        return restaurantRepository.findByOwner_UserId(userId)
                .stream()
                .map(RestaurantDTO::fromEntity)
                .collect(Collectors.toList());
    }

    /**
     * Updates a restaurant in the database.
     *
     * @param restaurantId The ID of the restaurant.
     * @param updateRestaurantDTO The updated restaurant data.
     * @return The updated {@link RestaurantDTO} object.
     */
    @Transactional
    public RestaurantDTO updateRestaurant(Integer restaurantId, UpdateRestaurantDTO updateRestaurantDTO) {
        System.out.println("Entered updateRestaurant method in RestaurantService");
        RestaurantEntity restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new UserNotFoundException("Restaurant not found"));


        // Update fields
        restaurant.setRestaurantName(updateRestaurantDTO.getRestaurantName());
        restaurant.setRestaurantPhone(updateRestaurantDTO.getRestaurantPhone());
        restaurant.setPicture(updateRestaurantDTO.getPicture());
        restaurant.setBusinessHours(updateRestaurantDTO.getBusinessHours());
        restaurant.setAddress(updateRestaurantDTO.getRestaurantAddress());
        // Save the updated restaurant entity
        RestaurantEntity updatedRestaurant = restaurantRepository.save(restaurant);

        return RestaurantDTO.fromEntity(updatedRestaurant);
    }

    /**
     * Retrieves all restaurants of a specific category from the database.
     *
     * @param categoryName The name of the category.
     * @return A list of {@link RestaurantDTO} objects representing all restaurants of the category in the database.
     */
    @Transactional
    public List<RestaurantDTO> getRestaurantsByCategory(String categoryName) {
        System.out.println("Entered getRestaurantsByCategory method in RestaurantService");
        List<RestaurantEntity> restaurants = restaurantRepository.findByCategoryName(categoryName);
        return restaurants.stream()
                .map(RestaurantDTO::fromEntity)
                .collect(Collectors.toList());
    }

}
