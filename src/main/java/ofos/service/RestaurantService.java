package ofos.service;

import ofos.dto.RestaurantDTO;
import ofos.entity.RestaurantEntity;
import ofos.repository.RestaurantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RestaurantService {

    private final RestaurantRepository restaurantRepository;

    @Autowired
    public RestaurantService(RestaurantRepository restaurantRepository) {
        this.restaurantRepository = restaurantRepository;
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

}
