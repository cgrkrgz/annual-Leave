// src/main/java/com/annualLeave/controller/AuthController.java
package com.annualLeave.controller;

import com.annualLeave.dto.*;
import com.annualLeave.model.RootEntity;
import com.annualLeave.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/auth") 
public class AuthController extends RestBaseController {

	@Autowired
	private AuthService authService;

	@PostMapping("/create")
	public RootEntity<DtoAuth> createUser(@RequestBody @Valid DtoAuthIU dtoAuthIU) {
		return ok(authService.createUser(dtoAuthIU));
	}

	@GetMapping("/tc/{tc}")
	public RootEntity<DtoAuth> getUserByTc(@PathVariable String tc) {
		return ok(authService.getUserByTc(tc));
	}

	@GetMapping("/all")
	public RootEntity<List<DtoAuth>> getAllUsers() {
		return ok(authService.getAllUsers());
	}

	@GetMapping("/exists/{tc}")
	public RootEntity<Boolean> userExists(@PathVariable String tc) {
		return ok(authService.userExists(tc));
	}

	@DeleteMapping("/delete/{id}")
	public RootEntity<String> deleteUser(@PathVariable Long id) {
		authService.deleteUser(id);
		return ok("Kullanıcı silindi");
	}

	@PostMapping("/login")
	public RootEntity<DtoLoginResponse> login(@RequestBody DtoLoginRequest request) {
		return ok(authService.login(request));
	}
}
