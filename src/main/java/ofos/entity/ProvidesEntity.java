package ofos.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Entity
@Getter
@Setter
@Table(name = "Provides")
@IdClass(ProvidesPK.class)
public class ProvidesEntity {

    @Id @Column(name="RestaurantID")
    private Integer RestaurantID;
    @Id @Column(name="ProductID")
    private Integer ProductID;

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        ProvidesEntity that = (ProvidesEntity) object;
        return RestaurantID == that.RestaurantID && ProductID == that.ProductID;
    }

    @Override
    public int hashCode() {
        return Objects.hash(RestaurantID, ProductID);
    }

    public ProvidesEntity(){
    }

}
