
package com.annualLeave.exception;

import lombok.Getter;

@Getter
public enum MessageType {
	
	NO_RECORD_EXIST("1001", "Kayıt bulunamadı"),
    GENERAL_EXCEPTION("9999", "Genel bir hata oluştu"),
    DUPLICATE_RECORD("1002", "Kayıt zaten mevcut"),
    INVALID_CREDENTIALS("1003", "Geçersiz kullanıcı bilgileri"),
    ACCESS_DENIED("1004", "Erişim reddedildi"),
    PASSWORD_TOO_WEAK("1005", "Şifre çok zayıf"),
    VALIDATION_EXCEPTION("1006", "Geçersiz veri");
	
	private final String code;
	
	private final String message;
	
	
	
	MessageType(String code, String message) {

		this.code = code;
		this.message = message;
		
	}

	public String getCode() { 
		return code; 
		}
    public String getMessage() { 
    	return message; 
    	}
	
}
