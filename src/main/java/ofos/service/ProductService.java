package ofos.service;

import ofos.dto.ProductDTO;
import ofos.entity.ProductEntity;
import ofos.entity.ProvidesEntity;
import ofos.repository.ProductRepository;
import ofos.repository.ProvidesRepository;
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

    public ProductEntity getDishById(int productId) {
        return productRepository.findByProductId(productId);
    }

    @Transactional
    public ResponseEntity<String> deleteDishById(int productId, String owner) {
        List<ProvidesEntity> ownedProducts = providesRepository.findProductOwnerByName(owner);
        for (ProvidesEntity product : ownedProducts) {
            if (productId == product.getProductID()) {
                productRepository.updateAvailability(productId);
                return new ResponseEntity<>(
                        "Product deleted.",
                        HttpStatus.OK
                );
            }
        }
        return new ResponseEntity<>(
                "Wrong product, wrong owner.",
                HttpStatus.UNAUTHORIZED
        );
    }

    public ProductEntity updateProduct(ProductDTO productDTO) {
        ProductEntity productEntity = productRepository.findByProductId(productDTO.getProductID());

        productEntity.setProductName(productDTO.getProductName());
        productEntity.setProductDesc(productDTO.getProductDesc());
        productEntity.setProductPrice(productDTO.getProductPrice());
        productEntity.setCategory(productDTO.getCategory());
        productEntity.setPicture(productDTO.getPicture());
        productEntity.setActive(true);



        return productRepository.save(productEntity);
    }

    public void createProduct(ProductDTO productDTO, int restaurantID) {
        ProductEntity productEntity = new ProductEntity();
        productEntity.setProductName(productDTO.getProductName());
        productEntity.setProductDesc(productDTO.getProductDesc());
        productEntity.setProductPrice(productDTO.getProductPrice());
        productEntity.setCategory(productDTO.getCategory());
        productEntity.setPicture(productDTO.getPicture());
        productEntity.setActive(true);
        productRepository.save(productEntity);

        // productID autoincrement nii pitää tehä näin (?)
        int productID = productRepository.findIdByName(productEntity.getProductName());

        // Heittää Provides taulukkoon datat.
        productRepository.addProductToRestaurant(restaurantID, productID);
    }

    public List<ProductEntity> getAllProductsByRestaurant(String restaurant) {
        return productRepository.getProductsByRestaurant(restaurant);
    }


}
