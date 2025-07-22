package com.annualLeave.handler;

import com.annualLeave.model.RootEntity;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.LocalDateTime;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void commence(HttpServletRequest request, 
                        HttpServletResponse response,
                        AuthenticationException authException) throws IOException {

        // JSON response hazırla
        RootEntity<String> errorResponse = new RootEntity<>();
        errorResponse.setSuccess(false);
        errorResponse.setMessage("Kimlik doğrulama başarısız! Token eksik veya geçersiz.");
        errorResponse.setData("Token gerekli. Lütfen giriş yapınız.");
        errorResponse.setHostName(getHostName());
        errorResponse.setPath(request.getRequestURI());
        errorResponse.setTimestamp(LocalDateTime.now());
        errorResponse.setStatusCode(401);

        // Response headers ayarla
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        // JSON olarak yaz
        response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
    }

    private String getHostName() {
        try {
            return InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException e) {
            return "unknown";
        }
    }
}