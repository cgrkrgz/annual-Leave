package com.annualLeave.config;

import com.annualLeave.model.Auth;
import com.annualLeave.repository.AnnualLeaveAuthRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class JwtUserDetailsService implements UserDetailsService {

    private final AnnualLeaveAuthRepository repository;

    @Override
    public UserDetails loadUserByUsername(String tc) throws UsernameNotFoundException {
        Auth user = repository.findByTc(tc)
                .orElseThrow(() -> new UsernameNotFoundException("Kullanıcı bulunamadı: " + tc));

        return new User(
                user.getTc(),
                user.getPassword(),
                List.of(new SimpleGrantedAuthority("ROLE_" + user.getUserType().toUpperCase()))
        );
    }
}
