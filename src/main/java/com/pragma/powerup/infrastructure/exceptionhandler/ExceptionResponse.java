package com.pragma.powerup.infrastructure.exceptionhandler;

public enum ExceptionResponse {
    NO_DATA_FOUND("No data found for the requested petition"),
    INVALID_FIELDS("Faltan campos obligatorios para la petición"),
    INVALID_RESTAURANT_NAME("Nombre del restaurante invalido"),
    INVALID_RESTAURANT_CELLPHONE("Celular de restaurante invalido"),
    INVALID_RESTAURANT_NIT("Nit de restaurante invalido"),
    INVALID_OWNER_ID("El id del usuario no corresponde al propietario"),
    INVALID_PRICE("El precio debe ser un valor entero mayor a 0"),
    INVALID_TOKEN("El token de autenticacion es invalido"),
    RESTAURANT_NOT_FOUND("Restaurante no encontrado"),
    CATEGORY_NOT_FOUND("Categoria no encontrada");

    private final String message;

    ExceptionResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return this.message;
    }
}