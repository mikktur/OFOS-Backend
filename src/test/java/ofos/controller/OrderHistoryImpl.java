package ofos.controller;

import lombok.Getter;
import lombok.Setter;
import ofos.repository.IOrderHistory;

import java.math.BigDecimal;

@Getter
@Setter
public class OrderHistoryImpl implements IOrderHistory {
    private int orderID;
    private BigDecimal productPrice;
    private int quantity;
    private String productName;

    public OrderHistoryImpl(int orderID, BigDecimal productPrice, int quantity, String productName) {
        this.orderID = orderID;
        this.productPrice = productPrice;
        this.quantity = quantity;
        this.productName = productName;
    }
}
