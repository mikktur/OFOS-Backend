package ofos.controller;

import ofos.dto.RestaurantDTO;
import ofos.dto.UpdateRestaurantDTO;
import ofos.entity.RestaurantEntity;
import ofos.service.RestaurantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/restaurants")
public class RestaurantController {

    private final RestaurantService restaurantService;

    @Autowired
    public RestaurantController(RestaurantService restaurantService) {
        this.restaurantService = restaurantService;
    }

    @PutMapping("/{restaurantId}")
    public ResponseEntity<RestaurantDTO> updateRestaurant(
            @PathVariable int restaurantId,
            @RequestBody UpdateRestaurantDTO updateRestaurantDTO) {
        RestaurantDTO updatedRestaurant = restaurantService.updateRestaurant(restaurantId, updateRestaurantDTO);
        return ResponseEntity.ok(updatedRestaurant);
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
