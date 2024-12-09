package ofos.service;

import ofos.dto.ProductDTO;
import ofos.entity.*;
import ofos.repository.ProductRepository;
import ofos.repository.ProvidesRepository;
import ofos.repository.RestaurantRepository;
import ofos.repository.TranslationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class ProductServiceTest {

    @InjectMocks
    ProductService productService;

    @Mock
    ProductRepository productRepository;

    @Mock
    TranslationRepository translationRepository;

    @Mock
    ProvidesRepository providesRepository;

    @Mock
    RestaurantRepository restaurantRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

   /* @Test
    void getDishByIdTest() {
        ProductEntity productEntity = new ProductEntity();
        when(productRepository.findByProductId(anyInt())).thenReturn(productEntity);

        ProductEntity result = productService.getDishById(1, "en");

        assertEquals(productEntity, result);
        verify(productRepository).findByProductId(anyInt());
    }*/

    @Test
    void deleteDishByIdTest() {
        ProvidesEntity providesEntity = new ProvidesEntity();
        providesEntity.setProductID(1);
        when(providesRepository.findProductOwnerByName(anyString())).thenReturn(Collections.singletonList(providesEntity));

        productService.deleteDishById(1, "owner");

        verify(productRepository).updateAvailability(anyInt());
    }

    @Test
    void updateProductTest() {
        int userId = 1;
        int restaurantId = 1;
        int productId = 1;


        ProductDTO productDTO = new ProductDTO();
        productDTO.setProductID(productId);
        productDTO.setProductName("Updated Product");
        productDTO.setProductPrice(BigDecimal.valueOf(10));
        productDTO.setTranslations(List.of(
                Map.of("language", "en", "name", "Updated Product", "description", "Updated Description")
        ));

        UserEntity owner = new UserEntity();
        owner.setUserId(userId);
        RestaurantEntity restaurant = new RestaurantEntity();
        restaurant.setOwner(owner);

        ProductEntity product = new ProductEntity();
        product.setProductId(productId);
        product.setProductName("Original Product");
        product.setTranslations(new ArrayList<>());

        when(restaurantRepository.findById(restaurantId)).thenReturn(Optional.of(restaurant));
        when(productRepository.findByProductIdAndRestaurantId(productId, restaurantId)).thenReturn(Optional.of(product));

        ResponseEntity<String> response = productService.updateProduct(productDTO, userId, restaurantId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Product updated.", response.getBody());

        verify(productRepository).save(any(ProductEntity.class));
        verify(restaurantRepository).findById(restaurantId);
        verify(productRepository).findByProductIdAndRestaurantId(productId, restaurantId);
    }

//    @Test
//    void createProductTest() {
//        String owner = "username";
//        ProductDTO productDTO = new ProductDTO(
//                1,
//                "Tuote",
//                "Hyvä tuote",
//                BigDecimal.valueOf(5),
//                "Kategoria",
//                "kuva.jpg"
//        );
//        List<RestaurantEntity> restaurants = new ArrayList<>();
//        RestaurantEntity restaurant = new RestaurantEntity();
//        restaurant.setAddress("Osoite 1");
//        restaurant.setRestaurantID(1);
//        restaurant.setRestaurantPhone("0505555555");
//        restaurant.setRestaurantName("Nimi");
//        restaurant.setPicture("logo.jpg");
//        restaurants.add(restaurant);
//
//        when(restaurantRepository.findRestaurantByOwnerName(owner)).thenReturn(restaurants);
//        ResponseEntity<String> res = productService.createProduct(productDTO,  1, owner);
//
//        assertEquals("Product created.", res.getBody());
//    }

    @Test
    @SuppressWarnings("unchecked")
    void getAllProductsByRestaurantTest() {
        int restaurantId = 1;
        String lang = "en";

        ProductEntity product = new ProductEntity();
        product.setProductId(1);
        product.setProductPrice(BigDecimal.valueOf(10));

        TranslationEntity translation = new TranslationEntity();
        translation.setName("Translated Name");
        translation.setDescription("Translated Description");

        List<Object[]> mockResult = new ArrayList<>();
        mockResult.add(new Object[]{product, translation});

        when(productRepository.findProductsWithTranslations(anyInt(), anyString())).thenReturn(mockResult);

        List<ProductDTO> result = productService.getAllProductsByRestaurant(restaurantId, lang);

        assertEquals(1, result.size());
        assertEquals("Translated Name", result.get(0).getProductName());
        assertEquals("Translated Description", result.get(0).getProductDesc());
    }
}
