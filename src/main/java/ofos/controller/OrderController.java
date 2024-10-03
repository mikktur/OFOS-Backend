package ofos.controller;


import jakarta.servlet.http.HttpServletRequest;
import ofos.dto.OrderDTO;
import ofos.dto.OrderHistoryDTO;
import ofos.entity.OrderProductsEntity;
import ofos.entity.OrdersEntity;
import ofos.repository.IOrderHistory;
import ofos.security.JwtUtil;
import ofos.service.OrdersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

/**
 * This class is used to handle the order requests.
 */
@RestController
@RequestMapping("/api/order")
public class OrderController {


    @Autowired
    OrdersService ordersService;

    @Autowired
    JwtUtil jwtUtil;

    /**
     * Saves a new order for a user.
     *
     * @param order The order to be saved.
     * @param req The HTTP request object.
     * @return A {@link ResponseEntity} object containing the status code.
     */
    @PostMapping
    @ResponseBody
    public ResponseEntity<String> makeOrder(@RequestBody List<OrderDTO> order, HttpServletRequest req){
        String jwt = req.getHeader("Authorization").substring(7);
        String username = jwtUtil.extractUsername(jwt);
        return ordersService.postOrder(order, username);
    }

    /**
     * Retrieves all orders for a user.
     *
     * @param id The ID of the user.
     * @return A list of {@link OrdersEntity} objects containing all orders of the user.
     */
    @GetMapping("/{id}")
    @ResponseBody
    public List<OrdersEntity> getOrdersByID(@PathVariable int id){
        return ordersService.getOrdersByUserID(id);
    }

    /**
     * Retrieves all order contents (products) for a user.
     *
     * @param id The ID of the user.
     * @return A list of {@link OrderProductsEntity} objects containing all order contents of the user.
     */
    @GetMapping("/products/{id}")
    @ResponseBody
    public List<OrderProductsEntity> getOrderProductsByID(@PathVariable int id){
        return ordersService.getOrderContentsByUserID(id);
    }

    // TODO:
    // Lisää päivämäärä hakutuloksiin.
//    @GetMapping("/history")
//    @ResponseBody
//    public List<IOrderHistory> getOrderHistory(HttpServletRequest req){
//        String jwt = req.getHeader("Authorization").substring(7);
//        String username = jwtUtil.extractUsername(jwt);
//        return ordersService.getHistory(username);
//    }

    /**
     * Retrieves the order history for a user.
     *
     * @param req The HTTP request object.
     * @return A {@link HashMap} object containing the order history.
     */
    @GetMapping("/history")
    @ResponseBody
    public HashMap<Integer, List<OrderHistoryDTO>> getOrderHistory(HttpServletRequest req){
        String jwt = req.getHeader("Authorization").substring(7);
        String username = jwtUtil.extractUsername(jwt);
        return ordersService.getHistory(username);
    }

    /**
     * Updates the status of an order.
     *
     * @param id The ID of the order.
     * @param status The new status of the order.
     * @return A {@link ResponseEntity} object containing the status code.
     */
    @PostMapping("/status/{id}/{status}")
    @ResponseBody
    public ResponseEntity<String> updateStatus(@PathVariable int id, @PathVariable String status){
        return ordersService.updateStatus(id, status);
    }

}
