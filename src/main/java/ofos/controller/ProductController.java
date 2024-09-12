package ofos.controller;

import jakarta.validation.Valid;
import ofos.dto.ProductDTO;
import ofos.entity.ProductEntity;
import ofos.security.JwtUtil;
import ofos.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private JwtUtil jwtUtil;


    @GetMapping("/{id}")
    @ResponseBody
    public ProductEntity getDishById(@PathVariable int id) {
        return productService.getDishById(id);
    }

    @GetMapping("/delete/{id}")
    @Transactional
//    public void deleteDish(@PathVariable int id, @RequestHeader String t) {
//        System.out.println(jwtUtil.extractUsername(t));
    // Validaatio
    public ResponseEntity<String> deleteDish(@PathVariable int id) {
        String hardcodedRestaurantOwnerUsername = "Jimi1";
        return productService.deleteDishById(id, hardcodedRestaurantOwnerUsername);

    }

    @PostMapping("/create")
    public ResponseEntity<String> createProduct(@Valid @RequestBody ProductDTO productDTO) {
        System.out.println("Taas");
        int hardcodedRestaurantID = 1;  // Path variablella varmaa.
        try {
            productService.createProduct(productDTO, hardcodedRestaurantID);
            return ResponseEntity.ok("Product added.");
        } catch (Exception e) {
            return new ResponseEntity<>(
                    "Oopsie woopsie.",
                    HttpStatus.BAD_REQUEST
            );
        }
    }

    @PostMapping("/update")
    public ResponseEntity<String> updateProduct(@Valid @RequestBody ProductDTO productDTO) {
        try {
            productService.updateProduct(productDTO);
            return ResponseEntity.ok("Product updated.");
        } catch (Exception e) {
            return new ResponseEntity<>(
                    "Oopsie woopsie.",
                    HttpStatus.BAD_REQUEST
            );
        }
    }

    @GetMapping("/restaurant/{restaurant}")
    @ResponseBody
    public List<ProductEntity> getProductsByRestaurant(@PathVariable String restaurant) {
        return productService.getAllProductsByRestaurant(restaurant);
    }


}
