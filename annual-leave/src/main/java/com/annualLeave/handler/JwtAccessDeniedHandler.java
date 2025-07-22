package com.annualLeave.handler;

import com.annualLeave.model.RootEntity;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.LocalDateTime;

@Component
public class JwtAccessDeniedHandler implements AccessDeniedHandler {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void handle(HttpServletRequest request,
                      HttpServletResponse response,
                      AccessDeniedException accessDeniedException) throws IOException {

        // JSON response hazırla
        RootEntity<String> errorResponse = new RootEntity<>();
        errorResponse.setSuccess(false);
        errorResponse.setMessage("Erişim reddedildi! Bu işlem için yetkiniz yok.");
        errorResponse.setData("ADMIN yetkisi gerekli.");
        errorResponse.setHostName(getHostName());
        errorResponse.setPath(request.getRequestURI());
        errorResponse.setTimestamp(LocalDateTime.now());
        errorResponse.setStatusCode(403);

        // Response headers ayarla
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);

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