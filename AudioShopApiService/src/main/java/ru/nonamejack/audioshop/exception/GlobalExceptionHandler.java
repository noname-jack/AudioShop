package ru.nonamejack.audioshop.exception;

import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.NoHandlerFoundException;
import ru.nonamejack.audioshop.dto.ApiResponse;
import ru.nonamejack.audioshop.exception.custom.*;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import com.fasterxml.jackson.databind.JsonMappingException.Reference;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;


@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ApiResponse<Object>> handleNotFound(NotFoundException ex) {
        return new ResponseEntity<>(
                ApiResponse.error(ex.getMessage(), HttpStatus.NOT_FOUND),
                HttpStatus.NOT_FOUND
        );
    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Object>> handleValidation(MethodArgumentNotValidException ex) {
        String errors = ex.getBindingResult().getFieldErrors().stream()
                .map(e -> e.getField() + ": " + e.getDefaultMessage())
                .collect(Collectors.joining("; "));
        return new ResponseEntity<>(
                ApiResponse.error(errors, HttpStatus.BAD_REQUEST),
                HttpStatus.BAD_REQUEST
        );
    }
    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ApiResponse<Object>> handleBadRequest(BadRequestException ex) {
        return new ResponseEntity<>(
                ApiResponse.error(ex.getMessage(), HttpStatus.BAD_REQUEST),
                HttpStatus.BAD_REQUEST
        );
    }
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiResponse<Object>> handleConstraintViolation(ConstraintViolationException ex) {
        String errors = ex.getConstraintViolations().stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.joining("; "));
        return new ResponseEntity<>(
                ApiResponse.error(errors, HttpStatus.BAD_REQUEST),
                HttpStatus.BAD_REQUEST
        );
    }
    // Универсальный обработчик ошибок разбора JSON
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiResponse<Object>> handleJsonParseError(HttpMessageNotReadableException ex) {
        Throwable cause = ex.getCause();
        if (cause instanceof InvalidFormatException invalidEx) {
            // Получаем целевой класс (например, enum-класс)
            Class<?> targetClass = invalidEx.getTargetType();
            if (Enum.class.isAssignableFrom(targetClass)) {
                @SuppressWarnings("unchecked")
                Class<? extends Enum<?>> enumType = (Class<? extends Enum<?>>) targetClass;

                // Список допустимых значений
                String allowed = Arrays.stream(enumType.getEnumConstants())
                        .map(Enum::name)
                        .collect(Collectors.joining(", "));

                // Имя поля из пути JSON
                String fieldName = invalidEx.getPath().stream()
                        .map(Reference::getFieldName)
                        .filter(Objects::nonNull)
                        .collect(Collectors.joining("."));

                String badValue = String.valueOf(invalidEx.getValue());

                String message = String.format(
                        "Неверное значение для поля '%s': '%s'. Допустимые значения: [%s]",
                        fieldName.isEmpty() ? targetClass.getSimpleName() : fieldName,
                        badValue,
                        allowed
                );

                return ResponseEntity
                        .badRequest()
                        .body(ApiResponse.error(message, HttpStatus.BAD_REQUEST));
            }
        }

        // Остальные ошибки парсинга JSON
        return ResponseEntity
                .badRequest()
                .body(ApiResponse.error("Некорректный формат JSON запроса", HttpStatus.BAD_REQUEST));
    }

    @ExceptionHandler(IncorrectUserFileException.class)
    public ResponseEntity<ApiResponse<Object>> handleImageFormat(IncorrectUserFileException ex){
        return new ResponseEntity<>(
                ApiResponse.error(ex.getMessage(), HttpStatus.UNSUPPORTED_MEDIA_TYPE),
                HttpStatus.UNSUPPORTED_MEDIA_TYPE
        );
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiResponse<Object>> handleAccessDenied(AccessDeniedException ex){
        return new ResponseEntity<>(
                ApiResponse.error(ex.getMessage(), HttpStatus.FORBIDDEN),
                HttpStatus.FORBIDDEN
        );
    }

    @ExceptionHandler(ResourceCreationException.class)
    public ResponseEntity<ApiResponse<Object>> handleAccessDenied(ResourceCreationException ex){
        return new ResponseEntity<>(
                ApiResponse.error(ex.getMessage(), HttpStatus.FORBIDDEN),
                HttpStatus.FORBIDDEN
        );
    }

    @ExceptionHandler(FileCreateException.class)
    public ResponseEntity<ApiResponse<Object>> handleAccessDenied(FileCreateException ex){
        return new ResponseEntity<>(
                ApiResponse.error(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR),
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<ApiResponse<Object>> handleNotFound(NoHandlerFoundException ex) {
        return new ResponseEntity<>(
                ApiResponse.error(ex.getMessage(), HttpStatus.NOT_FOUND),
                HttpStatus.NOT_FOUND
        );
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiResponse<Object>> handleIllegalArgumentException(IllegalArgumentException ex) {
        return new ResponseEntity<>(
                ApiResponse.error(ex.getMessage(), HttpStatus.BAD_REQUEST),
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(EntityNotEmptyException.class)
    public ResponseEntity<ApiResponse<Object>> handleEntityNotEmptyException(EntityNotEmptyException ex) {
        return  new ResponseEntity<>(
                ApiResponse.error(ex.getMessage(), HttpStatus.CONFLICT),
                HttpStatus.CONFLICT);
    }


}
