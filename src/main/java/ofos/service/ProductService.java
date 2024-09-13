package ofos.service;

import ofos.dto.ProductDTO;
import ofos.entity.ProductEntity;
import ofos.entity.ProvidesEntity;
import ofos.entity.RestaurantEntity;
import ofos.repository.ProductRepository;
import ofos.repository.ProvidesRepository;
import ofos.repository.RestaurantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProductService {

    @Autowired
    ProductRepository productRepository;

    @Autowired
    ProvidesRepository providesRepository;

    @Autowired
    RestaurantRepository restaurantRepository;



    public ProductEntity getDishById(int productId) {
        return productRepository.findByProductId(productId);
    }

    @Transactional
    public ResponseEntity<String> deleteDishById(int productId, String owner) {
        List<ProvidesEntity> ownedProducts = providesRepository.findProductOwnerByName(owner);
        if (!ownedProducts.isEmpty()) {
            for (ProvidesEntity product : ownedProducts) {
                if (productId == product.getProductID()) {
                    productRepository.updateAvailability(productId);
                    return new ResponseEntity<>(
                            "Product deleted.",
                            HttpStatus.OK
                    );
                }
            }
        }
        return new ResponseEntity<>(
                "Wrong product, wrong owner.",
                HttpStatus.UNAUTHORIZED
        );
    }

    public ResponseEntity<String> updateProduct(ProductDTO productDTO, String owner) {
        List<ProvidesEntity> ownedProducts = providesRepository.findProductOwnerByName(owner);
        if (!ownedProducts.isEmpty()) {
            for (ProvidesEntity product : ownedProducts) {
                if (productDTO.getProductID() == product.getProductID()) {
                    ProductEntity productEntity = productRepository.findByProductId(productDTO.getProductID());
                    productRepository.save(setValues(productDTO, productEntity));

                    return new ResponseEntity<>(
                            "Product updated.",
                            HttpStatus.OK
                    );
                }
            }
        }
        return new ResponseEntity<>(
                "You don't own that product.",
                HttpStatus.UNAUTHORIZED
        );
    }

    public ResponseEntity<String> createProduct(ProductDTO productDTO, int restaurantID, String owner) {
        List<RestaurantEntity> ownedRestaurants = restaurantRepository.findRestaurantByOwnerName(owner);
        if (!ownedRestaurants.isEmpty()) {
            for (RestaurantEntity re : ownedRestaurants) {
                if (re.getId() == restaurantID) {
                    ProductEntity productEntity = new ProductEntity();
                    productRepository.save(setValues(productDTO, productEntity));

                    // productID autoincrement nii pitää tehä näin (?)
                    int productID = productRepository.findIdByName(productEntity.getProductName());

                    // Heittää Provides taulukkoon datat.
                    productRepository.addProductToRestaurant(restaurantID, productID);
                    return new ResponseEntity<>(
                            "Product created.",
                            HttpStatus.OK
                    );
                }
            }
        }
        return new ResponseEntity<>(
                "Wrong owner or restaurant.",
                HttpStatus.UNAUTHORIZED
        );

    }

    public List<ProductEntity> getAllProductsByRestaurant(String restaurant) {
        return productRepository.getProductsByRestaurant(restaurant);
    }

    protected ProductEntity setValues(ProductDTO productDTO, ProductEntity productEntity) {
        productEntity.setProductName(productDTO.getProductName());
        productEntity.setProductDesc(productDTO.getProductDesc());
        productEntity.setProductPrice(productDTO.getProductPrice());
        productEntity.setCategory(productDTO.getCategory());
        productEntity.setPicture(productDTO.getPicture());
        productEntity.setActive(true);
        return productEntity;
    }

}
