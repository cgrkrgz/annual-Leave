package com.annualLeave.config;

import com.annualLeave.repository.AnnualLeaveAuthRepository;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.AuthenticationException;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final AnnualLeaveAuthRepository authRepository;
    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        // Login ve create endpoint'lerini atla
        String requestPath = request.getRequestURI();
        if (requestPath.equals("/api/auth/login") || requestPath.equals("/api/auth/create")) {
            filterChain.doFilter(request, response);
            return;
        }

        String authHeader = request.getHeader("Authorization");

        // Token yoksa devam et (Spring Security kendi exception'ını fırlatır)
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = authHeader.substring(7);

        try {
            if (jwtUtil.validateToken(token)) {
                String tc = jwtUtil.extractTc(token);

                // Kullanıcıyı UserDetailsService ile yükle
                UserDetails userDetails = userDetailsService.loadUserByUsername(tc);

                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,
                                userDetails.getAuthorities()
                        );

                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
            // Token geçersizse authentication'ı set etmiyoruz
            // Spring Security otomatik olarak AuthenticationEntryPoint'i çağırır
        } catch (Exception e) {
            // Log için
            System.out.println("JWT doğrulama hatası: " + e.getMessage());
            // Authentication'ı set etmiyoruz, Spring Security kendi exception'ını handle eder
        }

        filterChain.doFilter(request, response);
    }
}