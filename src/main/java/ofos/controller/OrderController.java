package ofos.controller;


import jakarta.servlet.http.HttpServletRequest;
import ofos.dto.OrderDTO;
import ofos.entity.OrderProductsEntity;
import ofos.entity.OrdersEntity;
import ofos.repository.IOrderHistory;
import ofos.security.JwtUtil;
import ofos.service.OrdersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/order")
public class OrderController {


    @Autowired
    OrdersService ordersService;

    @Autowired
    JwtUtil jwtUtil;

    @PostMapping
    @ResponseBody
    public ResponseEntity<String> makeOrder(@RequestBody List<OrderDTO> order, HttpServletRequest req){
        String jwt = req.getHeader("Authorization").substring(7);
        String username = jwtUtil.extractUsername(jwt);
        return ordersService.postOrder(order, username);
    }

    @GetMapping("/{id}")
    @ResponseBody
    public List<OrdersEntity> getOrdersByID(@PathVariable int id){
        return ordersService.getOrdersByUserID(id);
    }

    @GetMapping("/products/{id}")
    @ResponseBody
    public List<OrderProductsEntity> getOrderProductsByID(@PathVariable int id){
        return ordersService.getOrderContentsByUserID(id);
    }

    // TODO:
    // Lisää päivämäärä hakutuloksiin.
    @GetMapping("/history")
    @ResponseBody
    public List<IOrderHistory> getOrderHistory(HttpServletRequest req){
        String jwt = req.getHeader("Authorization").substring(7);
        String username = jwtUtil.extractUsername(jwt);
        return ordersService.getHistory(username);
    }

}
