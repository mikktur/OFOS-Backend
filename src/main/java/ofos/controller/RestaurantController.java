package ofos.controller;

import ofos.dto.RestaurantDTO;
import ofos.dto.UpdateRestaurantDTO;
import ofos.entity.RestaurantEntity;
import ofos.service.RestaurantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * This class is used to handle the restaurant requests.
 */
@RestController
@RequestMapping("/restaurants")
public class RestaurantController {

    private final RestaurantService restaurantService;

    @Autowired
    public RestaurantController(RestaurantService restaurantService) {
        this.restaurantService = restaurantService;
    }

    /**
     * Update a restaurant.
     * @param restaurantId The id of the restaurant.
     * @return A {@link RestaurantEntity} object containing the updated restaurant data.
     */
    @PutMapping("/{restaurantId}")
    public ResponseEntity<RestaurantDTO> updateRestaurant(
            @PathVariable int restaurantId,
            @RequestBody UpdateRestaurantDTO updateRestaurantDTO) {
        RestaurantDTO updatedRestaurant = restaurantService.updateRestaurant(restaurantId, updateRestaurantDTO);
        return ResponseEntity.ok(updatedRestaurant);
    }

    /**
     * Retrieves all restaurants.
     * @return A list of {@link RestaurantDTO} objects containing all restaurants.
     */
    @GetMapping
    public List<RestaurantDTO> getAllRestaurants() {
        return restaurantService.getAllRestaurants();
    }

    /**
     * Retrieves all restaurants owned by a user.
     * @param userId The ID of the user.
     * @return A list of {@link RestaurantDTO} objects containing all restaurants owned by the user.
     */
    @GetMapping("/owner/{userId}")
    public List<RestaurantDTO> getRestaurantsByOwner(@PathVariable int userId) {
        return restaurantService.getRestaurantsByOwner(userId);
    }

    /**
     * Retrieves all restaurants by category.
     * @param categoryName The name of the category.
     * @return A list of {@link RestaurantDTO} objects containing all restaurants in the category.
     */
    @GetMapping("/category/{categoryName}")
    public ResponseEntity<List<RestaurantDTO>> getRestaurantsByCategory(@PathVariable String categoryName) {
        List<RestaurantDTO> restaurants = restaurantService.getRestaurantsByCategory(categoryName);
        return ResponseEntity.ok(restaurants);
    }
}
