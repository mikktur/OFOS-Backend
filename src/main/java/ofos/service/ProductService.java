package ofos.service;

import ofos.dto.ProductDTO;
import ofos.entity.ProductEntity;
import ofos.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProductService {

    @Autowired
    ProductRepository productRepository;

    public ProductEntity getDishById(int productId) {
        return productRepository.findByProductId(productId);
    }

    @Transactional
    public void deleteDishById(int productId) {
        System.out.println("'Deleted' : " + productRepository.findByProductId(productId).getProductName());
        productRepository.updateAvailability(productId);
    }

    public ProductEntity saveProduct(ProductDTO productDTO) {
        ProductEntity productEntity;
        if (productDTO.getProductID() != 0) {
            productEntity = productRepository.findByProductId(productDTO.getProductID());
        } else {
            productEntity = new ProductEntity();
        }
        productEntity.setProductName(productDTO.getProductName());
        productEntity.setProductDesc(productDTO.getProductDesc());
        productEntity.setProductPrice(productDTO.getProductPrice());
        productEntity.setCategory(productDTO.getCategory());
        productEntity.setPicture(productDTO.getPicture());
        productEntity.setActive(true);


        return productRepository.save(productEntity);
    }

    public List<ProductEntity> getAllProductsByRestaurant(String restaurant) {
        return productRepository.getProductsByRestaurant(restaurant);
    }


}
