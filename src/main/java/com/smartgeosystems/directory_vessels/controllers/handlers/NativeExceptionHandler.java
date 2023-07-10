package com.smartgeosystems.directory_vessels.controllers.handlers;

import com.smartgeosystems.directory_vessels.dto.ExceptionMessageResponse;
import com.smartgeosystems.directory_vessels.dto.ExceptionValidationResponse;
import com.smartgeosystems.directory_vessels.exceptions.NotFoundException;
import com.smartgeosystems.directory_vessels.exceptions.VesselException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ControllerAdvice
public class NativeExceptionHandler {

    @ExceptionHandler(value = {NotFoundException.class, VesselException.class})
    public ResponseEntity<ExceptionMessageResponse> handleNotFoundExceptionAndVesselException(RuntimeException exception) {
        return ResponseEntity
                .badRequest()
                .headers(getHeaders())
                .body(new ExceptionMessageResponse(exception.getMessage()));
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleMethodArgumentNotValidException(MethodArgumentNotValidException exception) {
        Map<String, List<String>> errors = new HashMap<>();
        exception
                .getBindingResult()
                .getAllErrors()
                .forEach(objectError -> {
                    String fieldName = ((FieldError) objectError).getField();
                    List<String> messages = errors.get(fieldName);
                    final String errorMessage = objectError.getDefaultMessage();
                    if (messages != null) {
                        messages.add(errorMessage);
                        errors.put(fieldName, messages);
                    }
                    ArrayList<String> errorMessages = new ArrayList<>();
                    errorMessages.add(errorMessage);
                    errors.put(fieldName, errorMessages);
                });
        return ResponseEntity
                .badRequest()
                .headers(getHeaders())
                .body(new ExceptionValidationResponse(errors));
    }

    private HttpHeaders getHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }
}
