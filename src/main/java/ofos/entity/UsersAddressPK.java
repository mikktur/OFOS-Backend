package ofos.entity;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
public class UsersAddressPK implements Serializable {

    private Integer userId;
    private Integer deliveryAddressId;

    public UsersAddressPK() {}

    public UsersAddressPK(Integer userId, Integer deliveryAddressId) {
        this.userId = userId;
        this.deliveryAddressId = deliveryAddressId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UsersAddressPK that = (UsersAddressPK) o;
        return userId == that.userId && deliveryAddressId == that.deliveryAddressId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, deliveryAddressId);
    }
}
