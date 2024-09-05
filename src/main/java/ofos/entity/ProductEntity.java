package ofos.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "Products", schema = "mikt")
public class ProductEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ProductID", nullable = false)
    private Integer id;

    @Size(max = 255)
    @NotNull
    @Column(name = "ProductName", nullable = false)
    private String productName;

    @Size(max = 255)
    @NotNull
    @Column(name = "ProductDesc", nullable = false)
    private String productDesc;

    @NotNull
    @Column(name = "ProductPrice", nullable = false, precision = 10, scale = 2)
    private BigDecimal productPrice;

    @ManyToMany
    @JoinTable(name = "OrderProducts",
            joinColumns = @JoinColumn(name = "ProductID"),
            inverseJoinColumns = @JoinColumn(name = "OrderID"))
    private Set<OrderEntity> orders = new LinkedHashSet<>();

    @ManyToMany
    @JoinTable(name = "Provides",
            joinColumns = @JoinColumn(name = "ProductID"),
            inverseJoinColumns = @JoinColumn(name = "RestaurantID"))
    private Set<RestaurantEntity> restaurants = new LinkedHashSet<>();

}