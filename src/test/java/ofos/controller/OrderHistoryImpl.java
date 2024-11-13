package ofos.controller;

import lombok.Getter;
import lombok.Setter;
import ofos.repository.IOrderHistory;

import java.math.BigDecimal;
import java.sql.Date;

@Getter
@Setter
public class OrderHistoryImpl implements IOrderHistory {
    private int orderID;
    private BigDecimal productPrice;
    private int quantity;
    private String productName;
    private Date orderDate;
    private int restaurantID;
    private String productDesc;

    public OrderHistoryImpl(int orderID, BigDecimal productPrice, int quantity, String productName, Date orderDate, String productDesc) {
        this.orderID = orderID;
        this.productPrice = productPrice;
        this.quantity = quantity;
        this.productName = productName;
        this.orderDate = orderDate;
        this.productDesc = productDesc;
    }


    @Override
    public int getProductID() {
        return 0;
    }
}
