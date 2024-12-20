package ofos.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import ofos.dto.ProductDTO;
import ofos.entity.ProductEntity;
import ofos.security.JwtUtil;
import ofos.security.MyUserDetails;
import ofos.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
    public ProductDTO getDishById(@PathVariable int id) {
        return productService.getDishById(id);
    }

    @GetMapping("/{id}/{language}")
    @ResponseBody
    public ProductDTO getDishById(@PathVariable int id, @PathVariable String language) {
        return productService.getDishByIdAndLanguage(id, language);
    }



    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteDish(@PathVariable int id, HttpServletRequest request) {
        String authHead = request.getHeader("Authorization");
        String jwt = authHead.substring(7);
        System.out.println("JWT: " + jwtUtil.extractRole(jwt));
        if (jwtUtil.extractRole(jwt).equals("OWNER")) {
            String username = jwtUtil.extractUsername(jwt);
            return productService.deleteDishById(id, username);
        }
        return new ResponseEntity<>(
                "Not an owner.",
                HttpStatus.UNAUTHORIZED
        );
    }

    @DeleteMapping("/delete/{productId}/restaurant/{restaurantId}")
    public ResponseEntity<String> deleteDishFromRestaurant(
            @PathVariable int productId,
            @PathVariable int restaurantId,
            HttpServletRequest request) {

        String authHead = request.getHeader("Authorization");
        String jwt = authHead.substring(7);

        if (jwtUtil.extractRole(jwt).equals("OWNER")) {
            String username = jwtUtil.extractUsername(jwt);

            return productService.deleteProductFromRestaurant(productId, restaurantId, username);
        }

        return new ResponseEntity<>("Not an owner.", HttpStatus.UNAUTHORIZED);
    }



    @PostMapping("/create/{restaurantId}")
    public ResponseEntity<String> createProduct(@Valid @RequestBody ProductDTO productDTOs, @PathVariable int restaurantId,
                                                HttpServletRequest request) {
        System.out.println("ProductDTOs: " + productDTOs.getProductName());
        System.out.println("RestaurantId: " + restaurantId);
        System.out.println("Request: " + request);

        String jwt = request.getHeader("Authorization").substring(7);

        String username = jwtUtil.extractUsername(jwt);
        return productService.createProduct(productDTOs, restaurantId, username);
    }

    /**
     * Updates a product.
     *
     * @param productDTO  The product to be updated.
     * @param userDetails The authenticated user.
     * @param rid The id of the restaurant.
     * @return A {@link ResponseEntity} object containing the status code.
     */

    @PutMapping("/update/{rid}")
    @PreAuthorize("hasRole('Owner')")
    public ResponseEntity<String> updateProduct(@Valid @RequestBody ProductDTO productDTO, @PathVariable int rid,
                                                @AuthenticationPrincipal MyUserDetails userDetails) {
        int userId = userDetails.getUserId();
        try {
            return productService.updateProduct(productDTO, userId, rid);
        } catch (AccessDeniedException ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.UNAUTHORIZED);
        } catch (Exception ex) {
            return new ResponseEntity<>("An error occurred while updating the product.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }



    @GetMapping("/restaurant/{language}/{restaurant}")
    @ResponseBody
    public List<ProductDTO> getProductsByRestaurant(@PathVariable int restaurant, @PathVariable String language) {
        List<ProductDTO> products = productService.getAllProductsByRestaurant(restaurant, language);
        System.out.println("Ravintolan tuotteet: " + products);
        return products;
    }


}
