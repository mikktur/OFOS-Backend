package ofos.service;

import ofos.dto.RestaurantDTO;
import ofos.entity.RestaurantEntity;
import ofos.entity.UserEntity;
import ofos.repository.RestaurantRepository;
import ofos.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
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
        System.out.println("Restaurants retrieved: " + restaurants.get(0).getRestaurantName());
        return restaurants.stream()
                .map(restaurant -> new RestaurantDTO(
                        restaurant.getId(),
                        restaurant.getRestaurantName(),
                        restaurant.getRestaurantPhone(),
                        restaurant.getPicture(),
                        restaurant.getOwner().getUsername()
                ))
                .collect(Collectors.toList());
    }

    public List<RestaurantDTO> getRestaurantsByOwner(long userId) {
        System.out.println("Entered getRestaurantsByOwner method in RestaurantService");
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        System.out.println("User retrieved: " + user.getUsername() + " role: " + user.getRole());
        if (!"Owner".equals(user.getRole())) {
            throw new RuntimeException("User is not an owner");
        }

        return restaurantRepository.findByOwner_UserId(userId)
                .stream()
                .map(RestaurantDTO::fromEntity)
                .collect(Collectors.toList());
    }

}
