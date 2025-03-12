package com.pragma.powerup.domain.usecase.validations;

import com.pragma.powerup.domain.exception.InvalidFieldsException;
import com.pragma.powerup.domain.exception.InvalidPriceException;
import com.pragma.powerup.domain.model.Dish;


public class DishValidations {

    public void validateDish(Dish dish){
        validateFields(dish);
        validatePrice(dish);
    }


    private void validateFields(Dish dish) {

        if (dish.getName() == null || dish.getName().isBlank()) {
            throw new InvalidFieldsException();
        }
        if (dish.getPrice() == null) {
            throw new InvalidFieldsException();
        }
        if (dish.getDescription() == null || dish.getDescription().isBlank()) {
            throw new InvalidFieldsException();
        }
        if (dish.getImageUrl() == null || dish.getImageUrl().isBlank()) {
            throw new InvalidFieldsException();
        }
        if (dish.getCategory() == null) {
            throw new InvalidFieldsException();
        }
    }

    private void validatePrice (Dish dish){
        if (dish.getPrice() <= 0) {
            throw new InvalidPriceException();
        }
    }

}
