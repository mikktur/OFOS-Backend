package ofos.controller;

import ofos.dto.ProductDTO;
import ofos.entity.ProductEntity;
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

    @GetMapping("/{id}")
    @ResponseBody
    public ProductEntity getDishById(@PathVariable int id) {
        return productService.getDishById(id);
    }

    @GetMapping("/delete/{id}")
    @Transactional
    public void deleteDish(@PathVariable int id) {
        // Validaatio
        productService.deleteDishById(id);
    }

    @PostMapping("/create")
    public ResponseEntity<String> createProduct(@RequestBody ProductDTO productDTO) {
        try {
            productService.saveProduct(productDTO);
            return ResponseEntity.ok("Product added.");
        } catch (Exception e) {
            return new ResponseEntity<>(
                    "Oopsie woopsie.",
                    HttpStatus.BAD_REQUEST
            );
        }
    }

    // Vois suorittaa samalla pathilla ku /create
    // Näin luettavampaa (?)
    @PostMapping("/update")
    public ResponseEntity<String> updateProduct(@RequestBody ProductDTO productDTO) {
        try {
            productService.saveProduct(productDTO);
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
