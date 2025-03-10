package com.pragma.powerup.infrastructure.exceptionhandler;

public enum ExceptionResponse {
    NO_DATA_FOUND("No data found for the requested petition"),
    INVALID_RESTAURANT_FIELDS("Faltan campos obligatorios para la petición"),
    INVALID_RESTAURANT_NAME("Nombre del restaurante invalido"),
    INVALID_RESTAURANT_CELLPHONE("Celular de restaurante invalido"),
    INVALID_RESTAURANT_NIT("Nit de restaurante invalido"),
    INVALID_OWNER_ID("El id del usuario no corresponde a un propietario");

    private final String message;

    ExceptionResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return this.message;
    }
}