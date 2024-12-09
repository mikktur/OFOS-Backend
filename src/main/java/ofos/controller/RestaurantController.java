package ofos.controller;


import ofos.dto.RestaurantDTO;
import ofos.dto.UpdateRestaurantDTO;
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


    @GetMapping("/category/{categoryName}")
    public ResponseEntity<List<RestaurantDTO>> getRestaurantsByCategory(@PathVariable String categoryName) {
        List<RestaurantDTO> restaurants = restaurantService.getRestaurantsByCategory(categoryName);
        return ResponseEntity.ok(restaurants);
    }

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
    @PostMapping("/create")
    public ResponseEntity<String> createRestaurant(@RequestBody RestaurantDTO restaurantDTO) {
        try {
            restaurantService.createRestaurant(restaurantDTO);
            return ResponseEntity.ok("Restaurant created successfully.");
        } catch (RestaurantNotFoundException e) {

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred.");
        }
    }

}
