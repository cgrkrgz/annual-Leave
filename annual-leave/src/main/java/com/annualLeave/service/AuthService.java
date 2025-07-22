package com.annualLeave.service;

import com.annualLeave.config.JwtUtil;
import com.annualLeave.dto.DtoAuth;
import com.annualLeave.dto.DtoAuthIU;
import com.annualLeave.dto.DtoLoginRequest;
import com.annualLeave.dto.DtoLoginResponse;
import com.annualLeave.exception.BaseException;
import com.annualLeave.exception.ErrorMessage;
import com.annualLeave.exception.MessageType;
import com.annualLeave.model.Auth;
import com.annualLeave.repository.AnnualLeaveAuthRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class AuthService {

    @Autowired
    private AnnualLeaveAuthRepository authRepository;
    
    
    private boolean isPasswordStrong(String password) {
        if (password == null) return false;
        if (password.length() < 6) return false;

        boolean hasDigit = false;
        boolean hasLetter = false;

        for (char c : password.toCharArray()) {
            if (Character.isDigit(c)) hasDigit = true;
            if (Character.isLetter(c)) hasLetter = true;
        }

        return hasDigit && hasLetter;
    }


   

    public DtoAuth createUser(DtoAuthIU dtoAuthIU) {
    	if (authRepository.findByTc(dtoAuthIU.getTc()).isPresent()) {
            throw new BaseException(new ErrorMessage(MessageType.DUPLICATE_RECORD, dtoAuthIU.getTc()));
    	}
    	if (authRepository.findByEmail(dtoAuthIU.getEmail()).isPresent()) {
            throw new BaseException(new ErrorMessage(MessageType.DUPLICATE_RECORD, dtoAuthIU.getEmail()));
        }
    	if (!isPasswordStrong(dtoAuthIU.getPassword())) {
    	    throw new BaseException(new ErrorMessage(MessageType.PASSWORD_TOO_WEAK, "Şifreniz yeterince güçlü değil,sayı ve harf bulundurmalı"));
    	}

        Auth auth = new Auth();

        // Şifreyi BCrypt ile encode et
        String hashedPassword = passwordEncoder.encode(dtoAuthIU.getPassword());
        dtoAuthIU.setPassword(hashedPassword);

        BeanUtils.copyProperties(dtoAuthIU, auth);

        Auth savedUser = authRepository.save(auth);

        DtoAuth dto = new DtoAuth();
        BeanUtils.copyProperties(savedUser, dto);

        return dto;
    }

    public List<DtoAuth> getAllUsers() {
        List<Auth> userList = authRepository.findAll();
        List<DtoAuth> dtoList = new ArrayList<>();

        for (Auth user : userList) {
            DtoAuth dto = new DtoAuth();
            BeanUtils.copyProperties(user, dto);
            dtoList.add(dto);
        }

        return dtoList;
    }

    public DtoAuth getUserByTc(String tc) {
        Optional<Auth> optional = authRepository.findByTc(tc);
        if (optional.isPresent()) {
            DtoAuth dto = new DtoAuth();
            BeanUtils.copyProperties(optional.get(), dto);
            return dto;
        }
        throw new BaseException(new ErrorMessage(MessageType.NO_RECORD_EXIST, tc.toString()));
    }

    public boolean userExists(String tc) {
        return authRepository.findByTc(tc).isPresent();
    }

    public void deleteUser(Long id) {
        Optional<Auth> optional = authRepository.findById(id);
        if (optional.isPresent()) {
            authRepository.delete(optional.get());
        }

    }
    
    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public DtoLoginResponse login(DtoLoginRequest request) {
        Optional<Auth> optionalUser = authRepository.findByTc(request.getTc());

        if (optionalUser.isEmpty()) {
            throw new BaseException(new ErrorMessage(MessageType.NO_RECORD_EXIST, request.getTc()));
        }

        Auth user = optionalUser.get();

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new BaseException(new ErrorMessage(MessageType.INVALID_CREDENTIALS, "Şifre yanlış!"));
        }

        String token = jwtUtil.generateToken(user.getTc(), user.getUserType());

        DtoLoginResponse response = new DtoLoginResponse();
        response.setMessage("Giriş başarılı!");
        response.setUserType(user.getUserType());
        response.setToken(token);

        return response;
    }



    
    
}
