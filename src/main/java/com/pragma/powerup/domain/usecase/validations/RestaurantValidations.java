package com.pragma.powerup.domain.usecase.validations;

import com.pragma.powerup.domain.exception.*;
import com.pragma.powerup.domain.model.Restaurant;

public class RestaurantValidations {


    public void validateRestaurant(Restaurant restaurant){
        validateFields(restaurant);
        validateNit(restaurant);
        validateName(restaurant);
        validateCellPhone(restaurant);
    }

    public void validateOwnerRole(String role){
        if (!role.equals("PROPIETARIO")) {
            throw new InvalidOwnerException();
        }
    }


    private void validateFields(Restaurant restaurant) {

        if (restaurant.getName() == null || restaurant.getName().isBlank()) {
            throw new InvalidFieldsException();
        }
        if (restaurant.getNit() == null || restaurant.getNit().isBlank()) {
            throw new InvalidFieldsException();
        }
        if (restaurant.getAddress() == null || restaurant.getAddress().isBlank()) {
            throw new InvalidFieldsException();
        }
        if (restaurant.getPhone() == null || restaurant.getPhone().isBlank()) {
            throw new InvalidFieldsException();
        }
        if (restaurant.getLogoUrl() == null || restaurant.getLogoUrl().isBlank()) {
            throw new InvalidFieldsException();
        }
        if (restaurant.getOwnerId() == null) {
            throw new InvalidFieldsException();
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
