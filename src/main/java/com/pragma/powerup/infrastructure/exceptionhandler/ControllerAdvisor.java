package com.pragma.powerup.infrastructure.exceptionhandler;

import com.pragma.powerup.domain.exception.*;
import com.pragma.powerup.infrastructure.exception.NoDataFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Collections;
import java.util.Map;

@ControllerAdvice
public class ControllerAdvisor {

    private static final String MESSAGE = "message";

    @ExceptionHandler(NoDataFoundException.class)
    public ResponseEntity<Map<String, String>> handleNoDataFoundException(
            NoDataFoundException ignoredNoDataFoundException) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Collections.singletonMap(MESSAGE, ExceptionResponse.NO_DATA_FOUND.getMessage()));
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
    
}