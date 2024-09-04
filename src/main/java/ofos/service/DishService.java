package ofos.service;

import ofos.entity.DishEntity;
import ofos.repository.DishRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DishService {

    @Autowired
    DishRepository dishRepository;

    public DishEntity getDishById(int productId) {
        return dishRepository.findByProductId(productId);
    }

    public void deleteDishById(int productId) {
        System.out.println("Deleted: " + dishRepository.findByProductId(productId).getProductName());
        dishRepository.deleteByProductId(productId);
    }

}
