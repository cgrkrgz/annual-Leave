package com.annualLeave.repository;

import com.annualLeave.model.Leaves;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AnnualLeaveLeavesRepository extends JpaRepository<Leaves, Long> {
    
    // Belirli bir TC kimliğine göre izinleri bul
    List<Leaves> findByTc(String tc);

    // Belirli bir onay durumuna göre listele (isteğe bağlı)
    List<Leaves> findByIsapprove(boolean isapprove);
}
