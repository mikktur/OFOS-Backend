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


}
