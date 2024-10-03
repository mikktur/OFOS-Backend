package ofos.controller;


import jakarta.servlet.http.HttpServletRequest;
import ofos.dto.DeliveryAddressDTO;
import ofos.entity.DeliveryAddressEntity;
import ofos.entity.UsersAddressEntity;
import ofos.security.JwtUtil;
import ofos.service.DeliveryAddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * This class is used to handle the delivery address requests.
 */
@RestController
@RequestMapping("/api/deliveryaddress")
public class DeliveryAddressController {

    @Autowired
    DeliveryAddressService deliveryAddressService;

    @Autowired
    JwtUtil jwtUtil;

    /**
     * Retrieves all delivery addresses for a user.
     *
     * @param id The ID of the user.
     * @return A list of {@link DeliveryAddressDTO} objects containing all delivery addresses.
     */
    @GetMapping("/{id}")
    @ResponseBody
    public List<DeliveryAddressDTO> getDeliveryAddresses(@PathVariable int id){
        return deliveryAddressService.getDeliveryAddressesWithDefaultFlag(id);
    }

    /**
     * Saves a new delivery address for a user.
     *
     * @param deliveryAddressDTO The delivery address to be saved.
     * @param req The HTTP request object.
     * @return A {@link ResponseEntity} object containing the status code.
     */
    @PostMapping("/save")
    public ResponseEntity<String> saveDeliveryAddress(@RequestBody DeliveryAddressDTO deliveryAddressDTO, HttpServletRequest req){
        String jwt = req.getHeader("Authorization").substring(7);
        String username = jwtUtil.extractUsername(jwt);

        return deliveryAddressService.saveDeliveryAddress(deliveryAddressDTO, username);
    }

    /**
     * Updates a delivery address for a user.
     *
     * @param deliveryAddressDTO The delivery address to be updated.
     * @return A {@link ResponseEntity} object containing the status code.
     */
    @PutMapping("/update")
    public ResponseEntity<String> updateDeliveryAddress(@RequestBody DeliveryAddressDTO deliveryAddressDTO){
        return deliveryAddressService.updateDeliveryAddress(deliveryAddressDTO);
    }

    /**
     * Deletes a delivery address for a user.
     *
     * @param addressID The ID of the delivery address.
     * @param req The HTTP request object.
     * @return A {@link ResponseEntity} object containing the status code.
     */
    @DeleteMapping("/delete/{addressID}")
    public ResponseEntity<String> deleteDeliveryAddress(HttpServletRequest req, @PathVariable int addressID){
        String jwt = req.getHeader("Authorization").substring(7);
        String username = jwtUtil.extractUsername(jwt);
        return deliveryAddressService.deleteDeliveryAddress(addressID, username);
    }

    /**
     * Sets a delivery address as the default for a user.
     *
     * @param payload The delivery address ID and user ID.
     * @param req The HTTP request object.
     * @return A {@link ResponseEntity} object containing the status code.
     */
    @PutMapping("/setDefault")
    public ResponseEntity<String> setDefaultDeliveryAddress(@RequestBody Map<String, Integer> payload, HttpServletRequest req){
        String jwt = req.getHeader("Authorization").substring(7);
        String username = jwtUtil.extractUsername(jwt);

        int deliveryAddressId = payload.get("deliveryAddressId");
        int userId = payload.get("userId");

        return deliveryAddressService.setDefaultDeliveryAddress(deliveryAddressId, userId, username);
    }



}
