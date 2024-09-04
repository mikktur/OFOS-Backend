package ofos.controller;

import ofos.entity.DishEntity;
import ofos.service.DishService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/dishes")
public class DishController {

    @Autowired
    private DishService dishService;

    @GetMapping("/{id}")
    @ResponseBody
    public DishEntity getDishById(@PathVariable int id) {
        return dishService.getDishById(id);
    }

    @GetMapping("/delete/{id}")
    @Transactional
    public void deleteDish(@PathVariable int id) {
        dishService.deleteDishById(id);
    }

}
