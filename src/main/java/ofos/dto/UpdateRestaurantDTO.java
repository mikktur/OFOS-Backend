package ofos.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UpdateRestaurantDTO {

    private String restaurantName;

    private String restaurantPhone;

    private String picture;

    private String businessHours;

    private String restaurantAddress;


}
