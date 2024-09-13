package ofos.entity;

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
@Table(name = "Categories", schema = "mikt")
public class CategoryEntity {
    @Id
    @Column(name = "CategoryID", nullable = false)
    private Integer id;


    @Size(max = 255)
    @NotNull
    @Column(name = "CategoryName", nullable = false)
    private String categoryName;

    @ManyToMany
    @JoinTable(name = "Restaurant_Categories",
            joinColumns = @JoinColumn(name = "CategoryID"),
            inverseJoinColumns = @JoinColumn(name = "RestaurantID"))
    private Set<RestaurantEntity> restaurants = new LinkedHashSet<>();


}