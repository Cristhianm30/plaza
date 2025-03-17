package com.pragma.powerup.infrastructure.exceptionhandler;

public enum ExceptionResponse {
    NO_DATA_FOUND("No data found for the requested petition"),
    INVALID_FIELDS("Faltan campos obligatorios para la petición"),
    INVALID_RESTAURANT_NAME("Nombre del restaurante invalido"),
    INVALID_RESTAURANT_CELLPHONE("Celular de restaurante invalido"),
    INVALID_RESTAURANT_NIT("Nit de restaurante invalido"),
    INVALID_OWNER_ID("El ID del usuario no corresponde con el rol propietario"),
    INVALID_PRICE("El precio debe ser un valor entero mayor a 0"),
    INVALID_TOKEN("El token de autenticacion es invalido"),
    RESTAURANT_NOT_FOUND("Restaurante no encontrado"),
    CATEGORY_NOT_FOUND("Categoria no encontrada"),
    DISH_NOT_FOUND("Plato no encontrado"),
    HAS_ACTIVE_ORDER("El clienta ya tiene una orden activa"),
    MULTIPLE_RESTAURANT("El pedido no puede contener platos de diferentes Restaurantes"),
    INVALID_EMPLOYEE_ID("El ID del usuario no corresponde con el rol empleado"),
    INVALID_RESTAURANT_OWNER("El propietario no corresponde con el ID del restaurante");

    private final String message;

    ExceptionResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return this.message;
    }
}