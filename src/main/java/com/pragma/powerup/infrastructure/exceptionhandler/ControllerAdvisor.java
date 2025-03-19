package com.pragma.powerup.infrastructure.exceptionhandler;

import com.pragma.powerup.domain.exception.*;
import com.pragma.powerup.infrastructure.exception.OrderOtpNotFoundException;
import com.pragma.powerup.infrastructure.exception.RelationEmployeeRestaurantException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Collections;
import java.util.Map;

@ControllerAdvice
public class ControllerAdvisor {

    private static final String MESSAGE = "message";

    @ExceptionHandler(RelationEmployeeRestaurantException.class)
    public ResponseEntity<Map<String, String>> handleNoDataFoundException(
            RelationEmployeeRestaurantException ignoredRelationEmployeeRestaurantException) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Collections.singletonMap(MESSAGE, ExceptionResponse.EMPLOYEE_RELATION_NOT_FOUND.getMessage()));
    }

    @ExceptionHandler(InvalidFieldsException.class)
    public ResponseEntity<Map<String, String>> invalidFieldsException(
            InvalidFieldsException invalidException) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Collections.singletonMap(MESSAGE, ExceptionResponse.INVALID_FIELDS.getMessage()));
    }

    @ExceptionHandler(InvalidRestaurantCellPhoneException.class)
    public ResponseEntity<Map<String, String>> invalidRestaurantCellPhoneException(
            InvalidRestaurantCellPhoneException invalidRestaurantCellPhoneException) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Collections.singletonMap(MESSAGE, ExceptionResponse.INVALID_RESTAURANT_CELLPHONE.getMessage()));
    }

    @ExceptionHandler(InvalidRestaurantNameException.class)
    public ResponseEntity<Map<String, String>> invalidRestaurantNameException(
            InvalidRestaurantNameException invalidRestaurantNameException) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Collections.singletonMap(MESSAGE, ExceptionResponse.INVALID_RESTAURANT_NAME.getMessage()));
    }

    @ExceptionHandler(InvalidRestaurantNitException.class)
    public ResponseEntity<Map<String, String>> invalidRestaurantNitException(
            InvalidRestaurantNitException invalidRestaurantNitException) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Collections.singletonMap(MESSAGE, ExceptionResponse.INVALID_RESTAURANT_NIT.getMessage()));
    }

    @ExceptionHandler(InvalidOwnerException.class)
    public ResponseEntity<Map<String, String>> invalidOwnerException(
            InvalidOwnerException invalidOwnerException) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Collections.singletonMap(MESSAGE, ExceptionResponse.INVALID_OWNER_ID.getMessage()));
    }

    @ExceptionHandler(InvalidPriceException.class)
    public ResponseEntity<Map<String, String>> invalidPriceException(
            InvalidPriceException invalidPriceException) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Collections.singletonMap(MESSAGE, ExceptionResponse.INVALID_PRICE.getMessage()));
    }

    @ExceptionHandler(InvalidTokenException.class)
    public ResponseEntity<Map<String, String>> invalidTokenException(
            InvalidTokenException invalidTokenException) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Collections.singletonMap(MESSAGE, ExceptionResponse.INVALID_TOKEN.getMessage()));
    }

    @ExceptionHandler(RestaurantNotFoundException.class)
    public ResponseEntity<Map<String, String>> restaurantNotFoundException(
            RestaurantNotFoundException restaurantNotFoundException) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Collections.singletonMap(MESSAGE, ExceptionResponse.RESTAURANT_NOT_FOUND.getMessage()));
    }

    @ExceptionHandler(CategoryNotFoundException.class)
    public ResponseEntity<Map<String, String>> categoryNotFoundException(
            CategoryNotFoundException categoryNotFoundException) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Collections.singletonMap(MESSAGE, ExceptionResponse.CATEGORY_NOT_FOUND.getMessage()));
    }

    @ExceptionHandler(DishNotFoundException.class)
    public ResponseEntity<Map<String, String>> dishNotFoundException(
            DishNotFoundException dishNotFoundException) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Collections.singletonMap(MESSAGE, ExceptionResponse.DISH_NOT_FOUND.getMessage()));
    }

    @ExceptionHandler(HasActiveOrderException.class)
    public ResponseEntity<Map<String, String>> hasActiveOrderException(
            HasActiveOrderException hasActiveOrderException) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Collections.singletonMap(MESSAGE, ExceptionResponse.HAS_ACTIVE_ORDER.getMessage()));
    }

    @ExceptionHandler(MultipleRestaurantsException.class)
    public ResponseEntity<Map<String, String>> multipleRestaurantsException(
            MultipleRestaurantsException multipleRestaurantsException) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Collections.singletonMap(MESSAGE, ExceptionResponse.MULTIPLE_RESTAURANT.getMessage()));
    }

    @ExceptionHandler(InvalidEmployeeException.class)
    public ResponseEntity<Map<String, String>> invalidEmployeeException(
            InvalidEmployeeException invalidEmployeeException) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Collections.singletonMap(MESSAGE, ExceptionResponse.INVALID_EMPLOYEE_ID.getMessage()));
    }

    @ExceptionHandler(InvalidRestaurantOwnerException.class)
    public ResponseEntity<Map<String, String>> invalidRestaurantOwnerException(
            InvalidRestaurantOwnerException invalidRestaurantOwnerException) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Collections.singletonMap(MESSAGE, ExceptionResponse.INVALID_RESTAURANT_OWNER.getMessage()));
    }

    @ExceptionHandler(InvalidRestaurantEmployeeException.class)
    public ResponseEntity<Map<String, String>> invalidRestaurantEmployeeException(
            InvalidRestaurantEmployeeException invalidRestaurantEmployeeException) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Collections.singletonMap(MESSAGE, ExceptionResponse.INVALID_RESTAURANT_EMPLOYEE.getMessage()));
    }

    @ExceptionHandler(OrderOtpNotFoundException.class)
    public ResponseEntity<Map<String, String>> orderOtpNotFoundException(
            OrderOtpNotFoundException orderOtpNotFoundException) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Collections.singletonMap(MESSAGE, ExceptionResponse.OTP_NOT_FOUND.getMessage()));
    }

    @ExceptionHandler(InvalidOrderEmployeeException.class)
    public ResponseEntity<Map<String, String>> InvalidOrderEmployeeException(
            InvalidOrderEmployeeException InvalidOrderEmployeeException) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Collections.singletonMap(MESSAGE, ExceptionResponse.INVALID_EMPLOYEE_ORDER.getMessage()));
    }

    @ExceptionHandler(NotPreparationException.class)
    public ResponseEntity<Map<String, String>> NotPreparationException(
            NotPreparationException NotPreparationException) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Collections.singletonMap(MESSAGE, ExceptionResponse.NOT_PREPARATION.getMessage()));
    }

    @ExceptionHandler(NotPendingException.class)
    public ResponseEntity<Map<String, String>> NotPendingException(
            NotPendingException NotPendingException) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Collections.singletonMap(MESSAGE, ExceptionResponse.NOT_PENDING.getMessage()));
    }
    
}