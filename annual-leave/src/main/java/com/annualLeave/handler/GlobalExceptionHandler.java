package com.annualLeave.handler;

import com.annualLeave.exception.BaseException;
import com.annualLeave.exception.ErrorMessage;
import com.annualLeave.model.RootEntity;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.*;

@ControllerAdvice
public class GlobalExceptionHandler {

    // Validation hatası yakalama
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<RootEntity<Map<String, List<String>>>> handleValidationError(MethodArgumentNotValidException ex,HttpServletRequest request
    ) {
        Map<String, List<String>> errorMap = new HashMap<>();

        for (ObjectError error : ex.getBindingResult().getAllErrors()) {
            String field = ((FieldError) error).getField();
            if (!errorMap.containsKey(field)) {
                errorMap.put(field, new ArrayList<>());
            }
            errorMap.get(field).add(error.getDefaultMessage());

        }

        RootEntity<Map<String, List<String>>> response = RootEntity.errorWithData("Doğrulama hatası!",errorMap,getHostName(),request.getRequestURI(),400);

        return ResponseEntity.badRequest().body(response);
    }

    // Uygulama özel exception
    @ExceptionHandler(BaseException.class)
    public ResponseEntity<RootEntity<ErrorMessage>> handleBaseException(BaseException ex,HttpServletRequest request) {
        RootEntity<ErrorMessage> response = RootEntity.errorWithData(ex.getMessage(),ex.getErrorMessage(),getHostName(),request.getRequestURI(),400);

        return ResponseEntity.badRequest().body(response);
    }

    // Helper
    private String getHostName() {
        try {
            return InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException e) {
            return "unknown";
        }
    }
}
