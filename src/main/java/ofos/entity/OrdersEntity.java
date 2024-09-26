package ofos.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;

import java.sql.Date;
import java.util.Objects;

@Entity
@Getter
@Setter
@Table(name = "Orders", schema = "mikt")
public class OrdersEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "OrderID")
    private int orderId;
    @Basic
    @Column(name = "State")
    private String state;
    @Basic
    @Column(name = "OrderAddress")
    private String orderAddress;
    @Basic
    @CreatedDate
    @Column(name = "OrderDate")
    private Date orderDate;
    @Basic
    @Column(name = "User_ID")
    private int userId;
    @Basic
    @Column(name = "RestaurantID")
    private int restaurantId;

    public OrdersEntity(){}

    // Testiä varten
    public OrdersEntity(int orderId, String state, String orderAddress, Date orderDate, int userId, int restaurantId) {
        this.orderId = orderId;
        this.state = state;
        this.orderAddress = orderAddress;
        this.orderDate = orderDate;
        this.userId = userId;
        this.restaurantId = restaurantId;
    }
}
