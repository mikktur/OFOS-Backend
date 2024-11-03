package ofos.service;

import ofos.dto.ProductDTO;
import ofos.entity.ProductEntity;
import ofos.entity.ProvidesEntity;
import ofos.entity.RestaurantEntity;
import ofos.repository.ProductRepository;
import ofos.repository.ProvidesRepository;
import ofos.repository.RestaurantRepository;
import ofos.repository.TranslationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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

    @Test
    void getDishByIdTest() {
        ProductEntity productEntity = new ProductEntity();
        when(productRepository.findByProductId(anyInt())).thenReturn(productEntity);

        ProductEntity result = productService.getDishById(1, "en");

        assertEquals(productEntity, result);
        verify(productRepository).findByProductId(anyInt());
    }

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
        ProvidesEntity providesEntity = new ProvidesEntity();
        providesEntity.setProductID(1);
        ProductEntity productEntity = new ProductEntity();
        productEntity.setProductPrice(BigDecimal.valueOf(5));
        productEntity.setProductDesc("Namnam");
        productEntity.setProductName("Nimi");
        productEntity.setProductId(1);
        productEntity.setActive(true);
        productEntity.setCategory("Kategoria");
        productEntity.setPicture("kuva.jpg");


        when(providesRepository.findProductOwnerByName(anyString())).thenReturn(Collections.singletonList(providesEntity));
        when(productRepository.findByProductId(1)).thenReturn(productEntity);

        ProductDTO productDTO = new ProductDTO();
        productDTO.setProductID(1);
        ResponseEntity<String> res = productService.updateProduct(productDTO, "owner");

        verify(productRepository).save(any());
        assertEquals("Product updated.", res.getBody());
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
    void getAllProductsByRestaurantTest() {
        ProductEntity productEntity = new ProductEntity();
        List<ProductEntity> entities = new ArrayList<>();
        productEntity.setProductPrice(BigDecimal.valueOf(5));
        productEntity.setProductDesc("Namnam");
        productEntity.setProductName("Nimi");
        productEntity.setProductId(1);
        productEntity.setActive(true);
        productEntity.setCategory("Kategoria");
        productEntity.setPicture("kuva.jpg");
        entities.add(productEntity);


        when(productRepository.getProductsByRestaurant(anyInt())).thenReturn(entities);

        List<ProductDTO> res = productService.getAllProductsByRestaurant(1, "en");


        verify(productRepository).getProductsByRestaurant(anyInt());
        assertEquals(res.size(), 1);
    }
}