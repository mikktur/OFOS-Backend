package ofos.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * UserEntity class is an entity class that maps to the Users table in the database.
 * It contains the following columns:
 * - User_ID
 * - Username
 * - Password
 * - Role
 */
@Setter
@Getter
@Entity
@Table(name = "Users")
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "User_ID")
    private Integer userId;

    @Column(name = "Username")
    private String username;
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<OrdersEntity> orders = new ArrayList<>();
    @Column(name = "Password")
    private String password;

    @Column(name = "Role")
    private String role = "USER";
    @Column(name = "Enabled", nullable = true)
    private boolean enabled = true;
    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<RestaurantEntity> restaurants = new ArrayList<>();
    // Constructors
    public UserEntity() {
    }

    public UserEntity(int id,String username, String password, String role, boolean enabled) {
        this.userId = id;
        this.username = username;
        this.password = password;
        this.role = role;
        this.enabled = enabled;
    }

    public void addRestaurant(RestaurantEntity restaurant) {
        if (!restaurants.contains(restaurant)) {
            restaurants.add(restaurant);

        }
    }
}
