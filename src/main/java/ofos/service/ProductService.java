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

    @Transactional(readOnly = true)
    public ProductDTO getDishById(int productId) {
        ProductEntity product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        ProductDTO productDTO = new ProductDTO();
        productDTO.setProductID(product.getProductId());
        productDTO.setProductPrice(product.getProductPrice());
        productDTO.setCategory(product.getCategory());
        productDTO.setPicture(product.getPicture());
        productDTO.setTranslations(product.getTranslations().stream()
                .map(t -> Map.of(
                        "language", t.getId().getLang(),
                        "name", t.getName(),
                        "description", t.getDescription()
                )).collect(Collectors.toList()));

        return productDTO;

    }

    @Transactional
    public ProductDTO getDishByIdAndLanguage(int productId, String lang) {
        ProductEntity product = productRepository.findByProductId(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        TranslationEntity translation = translationRepository.findByProductAndLang(product, lang);
        if (translation == null) {
            throw new RuntimeException("Translation not found");
        }

        ProductDTO productDTO = new ProductDTO();
        productDTO.setProductID(product.getProductId());
        productDTO.setProductName(translation.getName());
        productDTO.setProductDesc(translation.getDescription());
        productDTO.setProductPrice(CurrencyConverter.convert("fi", lang, product.getProductPrice()));
        productDTO.setCategory(product.getCategory());
        productDTO.setPicture(product.getPicture());
        productDTO.setLang(lang);

        return productDTO;
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
        ProductEntity product = new ProductEntity();
        setValues(productDTO, product);

        productRepository.save(product);
        restaurant.addProduct(product);
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
            BigDecimal price = CurrencyConverter.convert("fi", lang, product.getProductPrice());
            productDTO.setProductPrice(price);
            productDTO.setCategory(product.getCategory());
            productDTO.setPicture(product.getPicture());
            productDTO.setLang(lang);

            return productDTO;
        }).collect(Collectors.toList());
    }


    protected void setValues(ProductDTO productDTO, ProductEntity productEntity) {
        productEntity.setCategory(productDTO.getCategory());
        productEntity.setPicture(productDTO.getPicture());
        productEntity.setProductPrice(productDTO.getProductPrice());
        productEntity.setActive(true);
        Map<String, TranslationEntity> existingTranslations = productEntity.getTranslations().stream()
                .collect(Collectors.toMap(t -> t.getId().getLang(), t -> t));
        for (Map<String, String> map : productDTO.getTranslations()) {
            String lang = map.get("language");
            String name = map.get("name");
            String description = map.get("description");
            System.out.println("Lang: " + lang);
            if (name.isEmpty() && description.isEmpty()) {
                continue;
            }

            if (existingTranslations.containsKey(lang)) {
                TranslationEntity translationEntity = existingTranslations.get(lang);
                if (name.equals(translationEntity.getName()) && description.equals(translationEntity.getDescription())) {
                    continue;
                }
                translationEntity.setName(name);
                translationEntity.setDescription(description);

            } else {

                TranslationEntity newTranslation = new TranslationEntity();
                newTranslation.setProduct(productEntity);
                newTranslation.getId().setLang(lang);
                newTranslation.setName(name);
                newTranslation.setDescription(description);
                productEntity.addTranslation(newTranslation);

            }
        }
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
