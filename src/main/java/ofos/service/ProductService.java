package ofos.service;

import ofos.dto.ProductDTO;
import ofos.entity.ProductEntity;
import ofos.entity.ProvidesEntity;
import ofos.entity.RestaurantEntity;
import ofos.entity.TranslationEntity;
import ofos.repository.ProductRepository;
import ofos.repository.ProvidesRepository;
import ofos.repository.RestaurantRepository;
import ofos.repository.TranslationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * This class provides methods to interact with and save the product data stored in the database.
 */
@Service
public class ProductService {

    @Autowired
    ProductRepository productRepository;

    @Autowired
    ProvidesRepository providesRepository;

    @Autowired
    RestaurantRepository restaurantRepository;

    @Autowired
    TranslationRepository translationRepository;


    /**
     * Retrieves products by their id from the database.
     * @param productId the id of the product.
     * @return A {@link ProductEntity} object representing the product with the ID.
     */

    public ProductEntity getDishById(int productId, String language) {
        ProductEntity product = productRepository.findByProductId(productId);
        if (language.equals("fi")) {
            return product;    // Oletus
        }
        TranslationEntity translated = translationRepository.findByProductIdAndLang(productId, language);
        product.setProductDesc(translated.getDescription());
        product.setProductName(translated.getName());
        return product;

    }


    /**
     * Deletes a product from the database.
     * @param productId the id of the product to be deleted.
     * @param owner the owner of the product.
     * @return {@link ResponseEntity} object with a message.
     */
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

    /**
     * Updates a product in the database.
     * @param productDTO the product to be updated.
     * @param owner the owner of the product.
     * @return {@link ResponseEntity} object with a message.
     */
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

    /**
     * Creates a product in the database.
     * @param productDTO the product to be created.
     * @param restaurantID the id of the restaurant.
     * @param owner the owner of the restaurant.
     * @return {@link ResponseEntity} object with a message.
     */
    public ResponseEntity<String> createProduct(ProductDTO productDTO, int restaurantID, String owner) {
        List<RestaurantEntity> ownedRestaurants = restaurantRepository.findRestaurantByOwnerName(owner);
        if (!ownedRestaurants.isEmpty()) {
            for (RestaurantEntity re : ownedRestaurants) {
                if (re.getRestaurantID() == restaurantID) {
                    ProductEntity productEntity = new ProductEntity();
                    productRepository.save(setValues(productDTO, productEntity));

                    // productId autoincrement nii pitää tehä näin (?)
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

    /**
     * Retrieves all products related to certain restaurant from the database.
     * @param id the id of the restaurant.
     * @return A list of {@link ProductDTO} objects representing all products in the database.
     */
    @Transactional(readOnly = true)
    public List<ProductDTO> getAllProductsByRestaurant(Integer id, String lang) {

        List<ProductEntity> products = productRepository.getProductsByRestaurant(id);

        if (!lang.equals("fi")){
            List<TranslationEntity> translations = translationRepository.findTranslationEntitiesByProductIdAndLang(id, lang);
            for (ProductEntity p : products){
                for (TranslationEntity t : translations){
                    if (p.getProductId() == t.getProductId()){
                        p.setProductName(t.getName());
                        p.setProductDesc(t.getDescription());
                    }
                }
            }
        }
        return products.stream()
                .map(product -> new ProductDTO(
                        product.getProductId(),
                        product.getProductName(),
                        product.getProductDesc(),
                        product.getProductPrice(),
                        product.getCategory(),
                        product.getPicture(),
                        product.isActive()))
                .collect(Collectors.toList());
    }

    /**
     * Sets the values of the product.
     * @param productDTO provides the values.
     * @param productEntity the object to be updated.
     * @return {@link ProductEntity} object with updated values.
     */
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
