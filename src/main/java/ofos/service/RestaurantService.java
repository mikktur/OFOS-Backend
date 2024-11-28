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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Service
public class RestaurantService {

    private final RestaurantRepository restaurantRepository;
    private final UserRepository userRepository;

    @Autowired
    public RestaurantService(RestaurantRepository restaurantRepository, UserRepository userRepository) {
        this.restaurantRepository = restaurantRepository;
        this.userRepository = userRepository;
    }


    public List<RestaurantDTO> getAllRestaurants() {
        System.out.println("Entered getAllRestaurants method in RestaurantService");
        List<RestaurantEntity> restaurants = restaurantRepository.findAll();
        // Print all restaurants retrieved from the repository
        System.out.println("Restaurants retrieved: ");

        return restaurants.stream()
                .map(RestaurantDTO::fromEntity)
                .collect(Collectors.toList());
    }


    public List<RestaurantDTO> getRestaurantsByOwner(int userId) {
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


    @Transactional
    public RestaurantDTO updateRestaurant(int restaurantId, UpdateRestaurantDTO updateRestaurantDTO) {
        System.out.println("Entered updateRestaurant method in RestaurantService");
        RestaurantEntity restaurant = restaurantRepository.findByRestaurantID(restaurantId)
                .orElseThrow(() -> new UserNotFoundException("Restaurant not found"));



        // Update fields                 it just works \_(?)_/¯
        if (updateRestaurantDTO.getRestaurantName() != null) {
            restaurant.setRestaurantName(updateRestaurantDTO.getRestaurantName());
        }

        if (updateRestaurantDTO.getRestaurantPhone() != null) {
            restaurant.setRestaurantPhone(updateRestaurantDTO.getRestaurantPhone());
        }

        if (updateRestaurantDTO.getPicture() != null) {
            restaurant.setPicture(updateRestaurantDTO.getPicture());
        }

        if (updateRestaurantDTO.getBusinessHours() != null) {
            restaurant.setBusinessHours(updateRestaurantDTO.getBusinessHours());
        }

        if (updateRestaurantDTO.getAddress() != null) {
            restaurant.setAddress(updateRestaurantDTO.getAddress());
        }

        // Save the updated restaurant entity
        RestaurantEntity updatedRestaurant = restaurantRepository.save(restaurant);

        return RestaurantDTO.fromEntity(updatedRestaurant);
    }


    @Transactional
    public List<RestaurantDTO> getRestaurantsByCategory(String categoryName) {
        System.out.println("Entered getRestaurantsByCategory method in RestaurantService");
        List<RestaurantEntity> restaurants = restaurantRepository.findByCategoryName(categoryName);
        return restaurants.stream()
                .map(RestaurantDTO::fromEntity)
                .collect(Collectors.toList());
    }

    public ResponseEntity<String> setNewOwner(int newOwnerId, int restaurantId){
        try {
            RestaurantEntity restaurant = restaurantRepository.findById(restaurantId)
                    .orElseThrow(() -> new RuntimeException("Restaurant not found"));
            UserEntity newOwner = userRepository.findByUserId(newOwnerId);
            int oldOwnerId = restaurant.getOwner().getUserId();
            restaurant.setOwner(newOwner);
            restaurantRepository.save(restaurant);

            // Jos uuden omistajan rooli ei ole entuudestaan "Owner"
            if (!newOwner.getRole().equalsIgnoreCase("Owner")){
                newOwner.setRole("OWNER");
                userRepository.save(newOwner);
            }

            // Jos vanhalla omistajalla ei ole ravintoloita.
            List<RestaurantEntity> ownedRestaurants = restaurantRepository.findByOwner_UserId(oldOwnerId);
            if (ownedRestaurants.isEmpty()){
                userRepository.setRoleToUser(oldOwnerId);
            }

            return new ResponseEntity<>(
                    "Owner updated.",
                    HttpStatus.OK
            );
        } catch (Exception e){
            return new ResponseEntity<>(
                    "Failed to update owner.",
                    HttpStatus.EXPECTATION_FAILED
            );
        }

    }


    @Transactional
    public void addRestaurantOwnerRole(int uid,int rid){
        UserEntity user = userRepository.findById(uid)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        RestaurantEntity restaurant = restaurantRepository.findById(rid)
                .orElseThrow(() -> new UserNotFoundException("Restaurant not found"));
        restaurant.setOwner(user);
        restaurantRepository.save(restaurant);

    }
}
