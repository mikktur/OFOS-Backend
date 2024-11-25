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

@RestController
@RequestMapping("/api/deliveryaddress")
public class DeliveryAddressController {

    @Autowired
    DeliveryAddressService deliveryAddressService;

    @Autowired
    JwtUtil jwtUtil;

    @GetMapping("/{id}")
    @ResponseBody
    public List<DeliveryAddressDTO> getDeliveryAddresses(@PathVariable int id){
        return deliveryAddressService.getDeliveryAddressesWithDefaultFlag(id);
    }

    @PostMapping("/save")
    public ResponseEntity<String> saveDeliveryAddress(@RequestBody DeliveryAddressDTO deliveryAddressDTO, HttpServletRequest req){
        String jwt = req.getHeader("Authorization").substring(7);
        String username = jwtUtil.extractUsername(jwt);

        return deliveryAddressService.saveDeliveryAddress(deliveryAddressDTO, username);
    }

    @PutMapping("/update")
    public ResponseEntity<String> updateDeliveryAddress(@RequestBody DeliveryAddressDTO deliveryAddressDTO){
        return deliveryAddressService.updateDeliveryAddress(deliveryAddressDTO);
    }


    @DeleteMapping("/delete/{addressID}")
    public ResponseEntity<String> deleteDeliveryAddress(HttpServletRequest req, @PathVariable int addressID){
        String jwt = req.getHeader("Authorization").substring(7);
        String username = jwtUtil.extractUsername(jwt);
        return deliveryAddressService.deleteDeliveryAddress(addressID, username);
    }


    @PutMapping("/setDefault")
    public ResponseEntity<String> setDefaultDeliveryAddress(@RequestBody Map<String, Integer> payload, HttpServletRequest req){
        String jwt = req.getHeader("Authorization").substring(7);
        String username = jwtUtil.extractUsername(jwt);

        int deliveryAddressId = payload.get("deliveryAddressId");
        int userId = payload.get("userId");

        return deliveryAddressService.setDefaultDeliveryAddress(deliveryAddressId, userId, username);
    }



}
