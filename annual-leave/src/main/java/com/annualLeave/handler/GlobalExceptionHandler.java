package com.annualLeave.handler;

import com.annualLeave.exception.BaseException;
import com.annualLeave.exception.ErrorMessage;
import com.annualLeave.model.RootEntity;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
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

    // ✅ Validation hatası
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<RootEntity<Map<String, List<String>>>> handleValidationError(MethodArgumentNotValidException ex, HttpServletRequest request) {
        Map<String, List<String>> errorMap = new HashMap<>();

        for (ObjectError error : ex.getBindingResult().getAllErrors()) {
            String field = ((FieldError) error).getField();
            if (!errorMap.containsKey(field)) {
                errorMap.put(field, new ArrayList<>());
            }
            List<String> messages = errorMap.get(field);
            messages.add(error.getDefaultMessage());

        }

        RootEntity<Map<String, List<String>>> response = RootEntity.errorWithData("Doğrulama hatası!", errorMap, getHostName(), request.getRequestURI(), 400);
        return ResponseEntity.badRequest().body(response);
    }

    // ✅ Özel exception (bizim yazdığımız BaseException)
    @ExceptionHandler(BaseException.class)
    public ResponseEntity<RootEntity<ErrorMessage>> handleBaseException(BaseException ex, HttpServletRequest request) {
        RootEntity<ErrorMessage> response = RootEntity.errorWithData(ex.getMessage(), ex.getErrorMessage(), getHostName(), request.getRequestURI(), 400);
        return ResponseEntity.badRequest().body(response);
    }

    // ✅ Token eksik/geçersiz (JWT authentication hatası)
    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<RootEntity<String>> handleAuthenticationException(AuthenticationException ex, HttpServletRequest request) {
        RootEntity<String> response = RootEntity.errorWithData("Kimlik doğrulama başarısız!", ex.getMessage(), getHostName(), request.getRequestURI(), 401);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }

    // ✅ Yetkisiz erişim (roller yetmiyor)
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<RootEntity<String>> handleAccessDeniedException(AccessDeniedException ex, HttpServletRequest request) {
        RootEntity<String> response = RootEntity.errorWithData("Erişim reddedildi!", ex.getMessage(), getHostName(), request.getRequestURI(), 403);
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
    }

    // ✅ Host bilgisini al
    private String getHostName() {
        try {
            return InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException e) {
            return "unknown";
        }
    }
}
