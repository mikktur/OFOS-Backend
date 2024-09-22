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

@RestController
@RequestMapping("/api/deliveryaddress")
public class DeliveryAddressController {

    @Autowired
    DeliveryAddressService deliveryAddressService;

    @Autowired
    JwtUtil jwtUtil;


    @GetMapping("/{id}")
    @ResponseBody
    public List<DeliveryAddressEntity> getDeliveryAddresses(@PathVariable int id){
        return deliveryAddressService.getDeliveryAddresses(id);
    }

    @PostMapping("/save")
    public ResponseEntity<String> saveDeliveryAddress(@RequestBody DeliveryAddressDTO deliveryAddressDTO, HttpServletRequest req){
        String jwt = req.getHeader("Authorization").substring(7);
        String username = jwtUtil.extractUsername(jwt);

        if (deliveryAddressService.saveDeliveryAddress(deliveryAddressDTO, username)) {
            return new ResponseEntity<>(
                    "Saved successfully.",
                    HttpStatus.OK
            );
        }
        return new ResponseEntity<>(
                "Something went wrong.",
                HttpStatus.BAD_REQUEST
        );
    }

    @PutMapping("/update")
    public ResponseEntity<String> updateDeliveryAddress(@RequestBody DeliveryAddressDTO deliveryAddressDTO){
        if (deliveryAddressService.updateDeliveryAddress(deliveryAddressDTO)) {
            return new ResponseEntity<>(
                    "Saved successfully.",
                    HttpStatus.OK
            );
        }
        return new ResponseEntity<>(
                "Something went wrong.",
                HttpStatus.BAD_REQUEST
        );
    }

    @DeleteMapping("/delete/{addressID}")
    public ResponseEntity<String> deleteDeliveryAddress(HttpServletRequest req, @PathVariable int addressID){
        String jwt = req.getHeader("Authorization").substring(7);
        String username = jwtUtil.extractUsername(jwt);
        if (deliveryAddressService.deleteDeliveryAddress(addressID, username)){
            return new ResponseEntity<>(
                    "Deleted successfully.",
                    HttpStatus.OK
            );
        }
        return new ResponseEntity<>(
                "Something went wrong.",
                HttpStatus.BAD_REQUEST
        );
    }


}
