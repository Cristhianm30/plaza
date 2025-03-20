package com.pragma.powerup.infrastructure.exceptionhandler;

public enum ExceptionResponse {
    EMPLOYEE_RELATION_NOT_FOUND("No se encontro la relacion empleado - restaurante"),
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
    INVALID_RESTAURANT_OWNER("El propietario no corresponde con el ID del restaurante"),
    INVALID_RESTAURANT_EMPLOYEE("El empleado no corresponde con el ID del restaurante"),
    OTP_NOT_FOUND("No se encontro el OTP en la mensajeria o en la base de datos"),
    INVALID_EMPLOYEE_ORDER("El empleado no esta acargo de la orden"),
    NOT_PREPARATION("La orden no esta en preparacion"),
    NOT_PENDING("La orden no esta pendiente"),
    WRONG_OTP("El codigo de verificacion es incorrecto"),
    NOT_READY("La orden no esta lista"),
    INVALID_CLIENT_ORDER("El cliente logueado no es quien realizo la order"),
    INVALID_CANCELING("Lo sentimos, tu pedido ya está en preparación y no puede cancelarse"),
    INVALID_ORDER_STATUS("EL estado de la orden tiene una inconsistencia ");

    private final String message;

    ExceptionResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return this.message;
    }
}