package ofos.controller;

import ofos.dto.RestaurantDTO;
import ofos.entity.RestaurantEntity;
import ofos.service.RestaurantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/restaurants")
public class RestaurantController {

    private final RestaurantService restaurantService;

    @Autowired
    public RestaurantController(RestaurantService restaurantService) {
        this.restaurantService = restaurantService;
    }

    @GetMapping
    public List<RestaurantDTO> getAllRestaurants() {
        return restaurantService.getAllRestaurants();
    }

    @GetMapping("/owner/{userId}")
    public List<RestaurantDTO> getRestaurantsByOwner(@PathVariable int userId) {
        return restaurantService.getRestaurantsByOwner(userId);
    }


    /*@GetMapping("/category/{categoryName}")
    public List<RestaurantEntity> getRestaurantsByCategory(@PathVariable String categoryName) {
        return restaurantService.getRestaurantsByCategory(categoryName);
    }*/
}
