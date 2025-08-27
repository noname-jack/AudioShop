package ru.nonamejack.audioshop.exception.custom;

public class EntityNotEmptyException extends RuntimeException {
    public EntityNotEmptyException(String message) {
        super(message);
    }
}
