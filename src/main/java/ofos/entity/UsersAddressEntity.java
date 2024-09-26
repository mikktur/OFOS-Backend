package ofos.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Objects;

@Entity
@Getter
@Setter
@IdClass(UsersAddressPK.class)
@Table(name = "UsersAddress", schema = "mikt")
public class UsersAddressEntity implements Serializable {
    @Id
    @Column(name = "User_ID", nullable = false)
    private int userId;


    @Id
    @Column(name = "DeliveryAddressID", nullable = false)
    private int deliveryAddressId;

    @Column(name = "is_default")
    private Boolean isDefault;

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        UsersAddressEntity that = (UsersAddressEntity) object;
        return userId == that.userId && deliveryAddressId == that.deliveryAddressId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, deliveryAddressId);
    }

}
