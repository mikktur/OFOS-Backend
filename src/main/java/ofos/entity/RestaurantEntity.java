package ofos.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "Restaurants", schema = "mikt")
public class RestaurantEntity {
    @Id
    @Column(name = "RestaurantID", nullable = false)
    private Integer id;

    @Size(max = 255)
    @NotNull
    @Column(name = "RestaurantName", nullable = false)
    private String restaurantName;

    @Size(max = 15)
    @NotNull
    @Column(name = "RestaurantPhone", nullable = false, length = 15)
    private String restaurantPhone;

    @Size(max = 255)
    @NotNull
    @Column(name = "Picture", nullable = false)
    private String picture;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "Owner", nullable = false)
    private UserEntity owner;

    @OneToMany(mappedBy = "restaurantID")
    private Set<OrderEntity> orders = new LinkedHashSet<>();

    /*
    @ManyToMany(mappedBy = "restaurants")
    private Set<ProductEntity> products = new LinkedHashSet<>();
    */

    @ManyToMany
    @JoinTable(name = "Restaurant_Categories",
            joinColumns = @JoinColumn(name = "RestaurantID"),
            inverseJoinColumns = @JoinColumn(name = "CategoryID"))
    private Set<CategoryEntity> categories = new LinkedHashSet<>();

}