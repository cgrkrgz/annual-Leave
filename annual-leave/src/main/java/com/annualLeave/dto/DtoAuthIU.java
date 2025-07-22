package com.annualLeave.dto;



import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class DtoAuthIU {
	
	@NotEmpty(message = "TC kısmı boş geçilemez")
	@Size(min = 11,max = 11,message = "TC 11 haneli olmalı")
    private String tc;
    
	@NotEmpty(message = "isim kısmı boş geçilemez")
    @Size(min=3 ,max= 20,message = "isim kısmı en az 3 en fazla 20 karakterli olmalı")
    private String firstName;

    @NotEmpty(message = "soyad kısmı boş geçilemez")
    @Size(min=3 ,max= 20,message = "soyad kısmı en az 3 en fazla 20 karakterli olmalı")
    private String lastName;
    
    @Size(min = 6 , message = "şifreniz en az 6 karakterli olmalıdır.")
    private String password;
    
    private String userType;
    
    @Email(message = "Email formatında bir adres giriniz!")
    private String email;
}