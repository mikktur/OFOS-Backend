package ofos.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "Orders")
public class OrderEntity {
    @Id
    @Column(name = "OrderID", nullable = false)
    private Integer id;

    @Size(max = 255)
    @NotNull
    @Column(name = "State", nullable = false)
    private String state;

    @Size(max = 255)
    @NotNull
    @Column(name = "OrderAddress", nullable = false)
    private String orderAddress;

    @NotNull
    @Column(name = "OrderDate", nullable = false)
    private LocalDate orderDate;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "RestaurantID", nullable = false)
    private RestaurantEntity restaurantID;

}