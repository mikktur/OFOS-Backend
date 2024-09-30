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
    public ResponseEntity<String> deleteDish(@PathVariable int id, HttpServletRequest request){
        String authHead = request.getHeader("Authorization");
        String jwt = authHead.substring(7);
        if (jwtUtil.extractRole(jwt).equals("Owner")) {
            String username = jwtUtil.extractUsername(jwt);
            return productService.deleteDishById(id, username);
        }
        return new ResponseEntity<>(
                "Not an owner.",
                HttpStatus.UNAUTHORIZED
        );

    }

    @PostMapping("/create/{restaurantId}")
    public ResponseEntity<String> createProduct(@Valid @RequestBody ProductDTO productDTO, @PathVariable int restaurantId,
                                                HttpServletRequest request) {
        String jwt = request.getHeader("Authorization").substring(7);
        String username = jwtUtil.extractUsername(jwt);
        System.out.println("heieieieie");
        return productService.createProduct(productDTO, restaurantId, username);
    }

    @PutMapping("/update")
    @PreAuthorize("hasRole('Owner')") // Ensure only users with the 'Owner' role can access this endpoint
    public ResponseEntity<String> updateProduct(@Valid @RequestBody ProductDTO productDTO,
                                                @AuthenticationPrincipal UserDetails userDetails) {
        String username = userDetails.getUsername();
        System.out.println("Username is: " + username);// Get the authenticated username
        return productService.updateProduct(productDTO, username);
    }

    @GetMapping("/restaurant/{restaurant}")
    @ResponseBody
    public List<ProductDTO> getProductsByRestaurant(@PathVariable Integer restaurant) {
        List<ProductDTO> products = productService.getAllProductsByRestaurant(restaurant);
        System.out.println("Ravintolan tuotteet: " + products);
        return products;
    }


}
