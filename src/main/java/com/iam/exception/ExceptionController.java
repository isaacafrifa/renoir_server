package com.iam.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.lang.Nullable;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@ControllerAdvice
public class ExceptionController extends ResponseEntityExceptionHandler {

    @ExceptionHandler(ResourceNotFound.class)
    public ResponseEntity<APIError> handleClientNotFoundException(ResourceNotFound ex, WebRequest request) {
        APIError errorDetails = new APIError(HttpStatus.NOT_FOUND.value(), ex.getMessage(),
                request.getDescription(false) + "", LocalDateTime.now());
        return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ResourceAlreadyExists.class)
    public ResponseEntity<APIError> handleClientAlreadyExistsException(ResourceAlreadyExists ex, WebRequest request) {
        APIError errorDetails = new APIError(HttpStatus.CONFLICT.value(), ex.getMessage(),
                request.getDescription(false) + "", LocalDateTime.now());
        return new ResponseEntity<>(errorDetails, HttpStatus.CONFLICT);
    }

    /*
      MethodArgumentTypeMismatchException: is thrown when method argument is
      not the expected type:
     */
    @ExceptionHandler({MethodArgumentTypeMismatchException.class})
    public ResponseEntity<Object> handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException ex,
                                                                   WebRequest request) {
        String error = "%s is not of the preferred type".formatted(ex.getName().toUpperCase());
        APIError apiError = new APIError(HttpStatus.BAD_REQUEST.value(), error,
                request.getDescription(false) + "", LocalDateTime.now());
        return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
    }


    /*
      MethodArgumentNotValidException: is thrown when argument annotated
      with @Valid failed validation returns HttpStatus.BAD_REQUEST as status
     */
    @Nullable
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers, HttpStatus status, WebRequest request) {
        List<String> errors = new ArrayList<>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.add(error.getField() + ": " + error.getDefaultMessage());
        }
        //remove array's [] from errors
        var errorString = errors.toString().replace("[", "").replace("]", "");
        APIError apiError = new APIError(status.value(), errorString,
                request.getDescription(false) + "", LocalDateTime.now());
        return handleExceptionInternal(ex, apiError, headers, status, request);
    }

    /*
     * Handle HttpMessageNotReadableException. Happens when request JSON is malformed.
     */
    @Nullable
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
                                                                  HttpHeaders headers, HttpStatus status, WebRequest request) {
        String error = "Malformed JSON request";
        APIError apiError = new APIError(HttpStatus.BAD_REQUEST.value(),
                error,
                request.getDescription(false) + "", LocalDateTime.now());
        return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
    }
}
