package com.annualLeave.dto;

import lombok.Data;

@Data
public class DtoLoginResponse {
    private String message;
    private String userType;
    private String token;

    
}
