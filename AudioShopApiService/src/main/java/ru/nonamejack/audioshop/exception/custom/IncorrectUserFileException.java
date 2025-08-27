package ru.nonamejack.audioshop.exception.custom;

public class IncorrectUserFileException extends RuntimeException{
    public IncorrectUserFileException(String message) {
        super(message);
    }
}
