package com.annualLeave.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.annualLeave.model.Auth;

public interface AnnualLeaveAuthRepository extends JpaRepository<Auth, Long>{
	Optional<Auth> findByTc(String tc);

	Optional<Auth> findByEmail(String email);
}
