package com.ecommerce.inventory_managment_system.exceptions;

public class BuisnessException extends  RuntimeException {
    private final String message;
    public BuisnessException(String message)
    {
        super(message);
        this.message = message;
    }
}
