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

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


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




    public ProductEntity getDishById(int productId, String language) {
        ProductEntity product = productRepository.findByProductId(productId);
        if (!language.equals("fi")) {

            TranslationEntity translated = translationRepository.findByProductAndLang(product, language);
            product.setProductDesc(translated.getDescription());
            product.setProductName(translated.getName());
        }
        return product;

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
public ResponseEntity<String> createProduct(ProductDTO productDTO, int restaurantId, String username) {
    // 1. Fetch the restaurant and validate ownership
    RestaurantEntity restaurant = restaurantRepository.findByRestaurantID(restaurantId)
            .orElseThrow(() -> new RuntimeException("Restaurant not found"));

    if (!restaurant.getOwner().getUsername().equals(username)) {
        return new ResponseEntity<>("You are not authorized to create products for this restaurant.", HttpStatus.UNAUTHORIZED);
    }

    // 2. Create ProductEntity
    ProductEntity product = new ProductEntity();
    product.setProductName(productDTO.getProductName());
    product.setProductPrice(productDTO.getProductPrice());
    product.setProductDesc(productDTO.getProductDesc());
    product.setPicture(productDTO.getPicture());
    product.setCategory(productDTO.getCategory());
    product.setActive(productDTO.isActive());

    ProductEntity savedProduct = productRepository.save(product);

    // 3. Save Translations
    List<Map<String, String>> translations = productDTO.getTranslations();
    translations.forEach(translation -> {
        String languageCode = translation.get("languageCode"); // e.g., "en"
        String name = translation.get("name");
        String description = translation.get("description");

        TranslationEntity translationEntity = new TranslationEntity();
        translationEntity.setProduct(savedProduct);
        translationEntity.setLang(languageCode);
        translationEntity.setName(name);
        translationEntity.setDescription(description);

        translationRepository.save(translationEntity);
    });

    // 4. Associate the product with the restaurant
    restaurant.addProduct(savedProduct);
    restaurantRepository.save(restaurant);

    return new ResponseEntity<>("Product created and associated with the restaurant successfully", HttpStatus.CREATED);
}





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
            BigDecimal price = product.getProductPrice();
            if (!lang.equals("fi")) {
                price = CurrencyConverter.convert("EUR",lang,product.getProductPrice());
            }
            productDTO.setProductPrice(price);
            productDTO.setCategory(product.getCategory());
            productDTO.setPicture(product.getPicture());
            productDTO.setLang(lang);

            return productDTO;
        }).collect(Collectors.toList());
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
