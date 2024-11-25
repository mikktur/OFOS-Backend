package ofos.controller;

import jakarta.servlet.http.HttpServletRequest;
import ofos.dto.RestaurantDTO;
import ofos.dto.UpdateRestaurantDTO;
import ofos.entity.RestaurantEntity;
import ofos.exception.RestaurantNotFoundException;
import ofos.exception.UserNotFoundException;
import ofos.security.JwtUtil;
import ofos.service.RestaurantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

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

    @Autowired
    JwtUtil jwtUtil;

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

    /**
     * Changes the owner of a restaurant.
     * @param payload A map containing the new owner ID and restaurant ID.
     * @return A response entity containing a message and status code.
     */
    @PutMapping("/changeowner")
    public ResponseEntity<String> changeOwner(@RequestBody Map<String, Object> payload){
        int ownerId = (int) payload.get("newOwnerId");
        int restaurantId = (int) payload.get("restaurantId");
        try {
            restaurantService.setNewOwner(ownerId, restaurantId);
            return ResponseEntity.ok("Owner updated successfully.");
        } catch (UserNotFoundException | RestaurantNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred.");
        }
    }
}
