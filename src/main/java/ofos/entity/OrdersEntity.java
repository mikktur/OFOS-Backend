package ofos.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "Orders", schema = "mikt")
public class OrdersEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "OrderID")
    private Integer orderId;
    @Basic
    @Column(name = "State")
    private String state;
    @Basic
    @Column(name = "OrderAddress")
    private String orderAddress;
    @Basic
    @Column(name = "OrderDate", insertable = false, updatable = false)
    private Date orderDate;
    @ManyToOne
    @JoinColumn(name = "User_ID")
    private UserEntity user;
    @ManyToOne
    @JoinColumn(name = "RestaurantID")
    private RestaurantEntity restaurant;
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL,fetch = FetchType.LAZY, orphanRemoval = true)
    private List<OrderProductsEntity> orderProducts = new ArrayList<>();
    public OrdersEntity(){}

    // Testiä varten
    public OrdersEntity(Integer orderId, String state, String orderAddress, Date orderDate, UserEntity user, RestaurantEntity restaurant) {
        this.orderId = orderId;
        this.state = state;
        this.orderAddress = orderAddress;
        this.orderDate = orderDate;
        this.user = user;
        this.restaurant = restaurant;
    }
}
