package ofos.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import ofos.dto.ProductDTO;
import ofos.entity.ProductEntity;
import ofos.security.JwtUtil;
import ofos.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * This class is used to handle the product requests.
 */
@RestController
@RequestMapping("/api/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private JwtUtil jwtUtil;

    /**
     * Retrieves a product by its id.
     * @param id The id of the product.
     * @return A {@link ProductEntity} object containing the product.
     */
    @GetMapping("/{id}")
    @ResponseBody
    public ProductEntity getDishById(@PathVariable int id) {
        return productService.getDishById(id);
    }

    /**
     * Deletes a product by its id.
     * @param id The id of the product.
     * @param request The HTTP request object.
     * @return A {@link ResponseEntity} object containing the status code.
     */
    @DeleteMapping("/delete/{id}")
    @Transactional
    public ResponseEntity<String> deleteDish(@PathVariable int id, HttpServletRequest request) {
        String authHead = request.getHeader("Authorization");
        String jwt = authHead.substring(7);
        System.out.println("JWT: " + jwtUtil.extractRole(jwt));
        if (jwtUtil.extractRole(jwt).equals("OWNER")) {
            String username = jwtUtil.extractUsername(jwt);
            System.out.println("haloo");
            return productService.deleteDishById(id, username);
        }
        return new ResponseEntity<>(
                "Not an owner.",
                HttpStatus.UNAUTHORIZED
        );
    }


    /**
     * Creates a new product for a certain restaurant.
     * @param productDTO The product to be created.
     * @param restaurantId The id of the restaurant.
     * @param request The HTTP request object.
     * @return A {@link ResponseEntity} object containing the status code.
     */
    @PostMapping("/create/{restaurantId}")
    public ResponseEntity<String> createProduct(@Valid @RequestBody ProductDTO productDTO, @PathVariable int restaurantId,
                                                HttpServletRequest request) {
        String jwt = request.getHeader("Authorization").substring(7);
        String username = jwtUtil.extractUsername(jwt);
        return productService.createProduct(productDTO, restaurantId, username);
    }

    /**
     * Updates a product.
     * @param productDTO The product to be updated.
     * @param userDetails The authenticated user.
     * @return A {@link ResponseEntity} object containing the status code.
     */
    @PutMapping("/update")
    @PreAuthorize("hasRole('Owner')") // Ensure only users with the 'Owner' role can access this endpoint
    public ResponseEntity<String> updateProduct(@Valid @RequestBody ProductDTO productDTO,
                                                @AuthenticationPrincipal UserDetails userDetails) {
        String username = userDetails.getUsername();
        System.out.println("Username is: " + username);// Get the authenticated username
        return productService.updateProduct(productDTO, username);
    }

    /**
     * Retrieves all products for a restaurant.
     * @param restaurant The id of the restaurant.
     * @return A list of {@link ProductDTO} objects containing all products of the restaurant.
     */
    @GetMapping("/restaurant/{restaurant}")
    @ResponseBody
    public List<ProductDTO> getProductsByRestaurant(@PathVariable Integer restaurant) {
        List<ProductDTO> products = productService.getAllProductsByRestaurant(restaurant);
        System.out.println("Ravintolan tuotteet: " + products);
        return products;
    }


}
