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
     *
     * @param productId the id of the product.
     * @return A {@link ProductEntity} object representing the product with the ID.
     */

    public ProductEntity getDishById(int productId, String language) {
        ProductEntity product = productRepository.findByProductId(productId);
        if (!language.equals("fi")) {

            TranslationEntity translated = translationRepository.findByProductAndLang(product, language);
            product.setProductDesc(translated.getDescription());
            product.setProductName(translated.getName());
        }
        return product;

    }


    /**
     * Deletes a product from the database.
     *
     * @param productId the id of the product to be deleted.
     * @param owner     the owner of the product.
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
     *
     * @param productDTO the product to be updated.
     * @param userId     the owner of the product.
     * @param restaurantId the id of the restaurant.
     * @return {@link ResponseEntity} object with a message.
     */
    @Transactional
    public ResponseEntity<String> updateProduct(ProductDTO productDTO, int userId, int restaurantId) {

        RestaurantEntity restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new RuntimeException("Restaurant not found"));

        if (!restaurant.getOwner().getUserId().equals(userId)) {
            return new ResponseEntity<>("You are not the owner of this restaurant.", HttpStatus.UNAUTHORIZED);
        }

        ProductEntity product = productRepository.findByProductIdAndRestaurantId(productDTO.getProductID(), restaurantId)
                .orElseThrow(() -> new RuntimeException("The specified product does not belong to this restaurant."));

        TranslationEntity translation = translationRepository.findByProductAndLang(product, productDTO.getLang());
        if (translation == null) {
            translation = new TranslationEntity();
            translation.setProduct(product);
            translation.getId().setLang(productDTO.getLang());
            translation.getId().setProductId(product.getProductId());
            product.addTranslation(translation);
        }

        translation.setName(productDTO.getProductName());
        translation.setDescription(productDTO.getProductDesc());

        setValues(productDTO, product);

        productRepository.save(product);

        return new ResponseEntity<>("Product updated.", HttpStatus.OK);
    }
    /**
     * Creates a product in the database.
     *
     * @param productDTOs  the product to be created.
     * @param restaurantID the id of the restaurant.
     * @param owner        the owner of the restaurant.
     * @return {@link ResponseEntity} object with a message.
     */
//    public ResponseEntity<String> createProduct(ProductDTO productDTO, int restaurantID, String owner) {
//        List<RestaurantEntity> ownedRestaurants = restaurantRepository.findRestaurantByOwnerName(owner);
//        if (!ownedRestaurants.isEmpty()) {
//            for (RestaurantEntity re : ownedRestaurants) {
//                if (re.getRestaurantID() == restaurantID) {
//                    ProductEntity productEntity = new ProductEntity();
//                    productRepository.save(setValues(productDTO, productEntity));
//
//                    // productId autoincrement nii pitää tehä näin (?)
//                    int productID = productRepository.findIdByName(productEntity.getProductName());
//                    // Heittää Provides taulukkoon datat.
//                    productRepository.addProductToRestaurant(restaurantID, productID);
//                    return new ResponseEntity<>(
//                            "Product created.",
//                            HttpStatus.OK
//                    );
//                }
//            }
//        }
//        return new ResponseEntity<>(
//                "Wrong owner or restaurant.",
//                HttpStatus.UNAUTHORIZED
//        );
//
//    }
    @Transactional
    public ResponseEntity<String> createProduct(ProductDTO productDTOs, int restaurantID, String owner) {

        RestaurantEntity restaurant = restaurantRepository.findByRestaurantID(restaurantID)
                .orElseThrow(() -> new RuntimeException("Restaurant not found"));

        if (!restaurant.getOwner().getUsername().equals(owner)) {
            return new ResponseEntity<>("You are not authorized to create products for this restaurant.", HttpStatus.UNAUTHORIZED);
        }

        ProductEntity productEntity = new ProductEntity();
        setValues(productDTOs, productEntity);
        TranslationEntity translation = new TranslationEntity(
                productEntity,
                productDTOs.getLang(),
                productDTOs.getProductName(),
                productDTOs.getProductDesc()
        );

       productEntity.addTranslation(translation);
       restaurant.addProduct(productEntity);
       restaurantRepository.save(restaurant);

        return new ResponseEntity<>("Product created.", HttpStatus.OK);
    }

    /**
     * Retrieves all products related to certain restaurant from the database.
     *
     * @param id the id of the restaurant.
     * @return A list of {@link ProductDTO} objects representing all products in the database.
     */
    @Transactional(readOnly = true)
    public List<ProductDTO> getAllProductsByRestaurant(int id, String lang) {
        List<Object[]> results = productRepository.findProductsWithTranslations(id, lang);
        return results.stream().map(result -> {
            ProductEntity product = (ProductEntity) result[0];
            TranslationEntity translation = (TranslationEntity) result[1];

            ProductDTO productDTO = new ProductDTO();
            productDTO.setProductID(product.getProductId());
            productDTO.setProductName(translation != null ? translation.getName() : null);
            productDTO.setProductDesc(translation != null ? translation.getDescription() : null);
            productDTO.setProductPrice(product.getProductPrice());
            productDTO.setCategory(product.getCategory());
            productDTO.setPicture(product.getPicture());
            productDTO.setLang(lang);

            return productDTO;
        }).collect(Collectors.toList());
    }

    /**
     * Sets the values of the product.
     *
     * @param productDTO    provides the values.
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

    /**
     * Deletes a product from a specific restaurant.
     *
     * @param productId    the ID of the product to be deleted.
     * @param restaurantId the ID of the restaurant from which the product will be removed.
     * @param owner        the username of the owner to verify authorization.
     * @return {@link ResponseEntity} object with a message indicating the result of the deletion.
     */
    @Transactional
    public ResponseEntity<String> deleteProductFromRestaurant(int productId, int restaurantId, String owner) {
        RestaurantEntity restaurant = restaurantRepository.findByRestaurantID(restaurantId)
                .orElseThrow(() -> new RuntimeException("Restaurant not found"));

        if (!restaurant.getOwner().getUsername().equals(owner)) {
            return new ResponseEntity<>("You are not authorized to delete products from this restaurant.", HttpStatus.UNAUTHORIZED);
        }

        ProvidesEntity providesEntity = providesRepository.findByProductIdAndRestaurantId(productId, restaurantId);

        if (providesEntity == null) {
            return new ResponseEntity<>("Product not found in this restaurant.", HttpStatus.NOT_FOUND);
        }

        providesRepository.delete(providesEntity);

        return new ResponseEntity<>("Product successfully deleted from the restaurant.", HttpStatus.OK);
    }


}
