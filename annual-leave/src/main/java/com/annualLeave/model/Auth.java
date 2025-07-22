package com.annualLeave.model;

import jakarta.persistence.*;
import lombok.*;




@Entity
@Table(name = "auth")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Auth {

	@Id
    @Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // UUID yerine String
	
	@Column(name = "first_name",nullable = false)
	private String firstName;

	@Column(name = "last_name",nullable = false)
	private String lastName;
	
    @Column(name = "tc", unique = true, nullable = false)
    private String tc;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "user_type", nullable = false)
    private String userType;
    
    @Column(name = "email",unique = true,nullable = false)
    private String email;
    
}
