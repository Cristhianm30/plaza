package com.pragma.powerup.domain.usecase.validations;

import com.pragma.powerup.domain.exception.InvalidRestaurantCellPhoneException;
import com.pragma.powerup.domain.exception.InvalidRestaurantNameException;
import com.pragma.powerup.domain.exception.InvalidRestaurantNitException;
import com.pragma.powerup.domain.exception.InvalidRestaurantFieldsException;
import com.pragma.powerup.domain.model.Restaurant;

public class RestaurantValidations {


    public void validateRestaurant(Restaurant restaurant){
        validateFields(restaurant);
        validateNit(restaurant);
        validateName(restaurant);
        validateCellPhone(restaurant);
    }


    private void validateFields(Restaurant restaurant) {

        if (restaurant.getName() == null || restaurant.getName().isBlank()) {
            throw new InvalidRestaurantFieldsException();
        }
        if (restaurant.getNit() == null || restaurant.getNit().isBlank()) {
            throw new InvalidRestaurantFieldsException();
        }
        if (restaurant.getAddress() == null || restaurant.getAddress().isBlank()) {
            throw new InvalidRestaurantFieldsException();
        }
        if (restaurant.getPhone() == null || restaurant.getPhone().isBlank()) {
            throw new InvalidRestaurantFieldsException();
        }
        if (restaurant.getLogoUrl() == null || restaurant.getLogoUrl().isBlank()) {
            throw new InvalidRestaurantFieldsException();
        }
        if (restaurant.getOwnerId() == null) {
            throw new InvalidRestaurantFieldsException();
        }
    }

    private void validateNit(Restaurant restaurant) {

        if (restaurant.getNit() == null || !restaurant.getNit().matches("^\\d+$")) {
            throw new InvalidRestaurantNitException();
        }
    }

    private void validateCellPhone (Restaurant restaurant) {

        if (restaurant.getPhone() == null || !restaurant.getPhone().matches("^\\+?\\d{1,13}$")) {
            throw new InvalidRestaurantCellPhoneException();
        }
    }

    private void validateName (Restaurant restaurant) {

        if (restaurant.getName() == null || restaurant.getName().matches("^\\d*$")) {
            throw new InvalidRestaurantNameException();
        }
    }




}
