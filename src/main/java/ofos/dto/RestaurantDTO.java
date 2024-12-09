package ofos.dto;

import ofos.entity.RestaurantEntity;

public class RestaurantDTO {
    private Integer id;
    private String restaurantName;
    private String restaurantPhone;
    private String picture;
    private String ownerUsername;
    private String address;
    private String businessHours;
    private Integer ownerId;

    public RestaurantDTO(Integer id, String restaurantName, String restaurantPhone, String picture, String ownerUsername, String address, String businessHours) {
        this.id = id;
        this.restaurantName = restaurantName;
        this.restaurantPhone = restaurantPhone;
        this.picture = picture;
        this.address = address;
        this.ownerUsername = ownerUsername;
        this.businessHours = businessHours;
    }

    public RestaurantDTO() {
    }

    // Getters and Setters

    public Integer getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Integer ownerId) {
        this.ownerId = ownerId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getRestaurantName() {
        return restaurantName;
    }

    public void setRestaurantName(String restaurantName) {
        this.restaurantName = restaurantName;
    }

    public String getRestaurantPhone() {
        return restaurantPhone;
    }

    public void setRestaurantPhone(String restaurantPhone) {
        this.restaurantPhone = restaurantPhone;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getOwnerUsername() {

        return ownerUsername;
    }

    public void setOwnerUsername(String ownerUsername) {
        this.ownerUsername = ownerUsername;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getBusinessHours() {
        return businessHours;
    }

    public void setBusinessHours(String businessHours) {
        this.businessHours = businessHours;
    }


    public static RestaurantDTO fromEntity(RestaurantEntity restaurant) {
        return new RestaurantDTO(
                restaurant.getRestaurantID(),
                restaurant.getRestaurantName(),
                restaurant.getRestaurantPhone(),
                restaurant.getPicture(),
                restaurant.getOwner().getUsername(),
                restaurant.getAddress(),
                restaurant.getBusinessHours()
        );
    }

}
