package com.annualLeave.controller;

import com.annualLeave.dto.LeavesRequestDTO;
import com.annualLeave.dto.LeavesResponseDTO;
import com.annualLeave.model.RootEntity;
import com.annualLeave.service.LeavesService;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/leaves")
public class LeavesController extends RestBaseController {

    @Autowired
    private LeavesService leavesService;

    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @PostMapping("/create")
    public RootEntity<LeavesResponseDTO> createLeave(@RequestBody @Valid LeavesRequestDTO dto) {
        return ok(leavesService.createLeave(dto));
    }
    
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/all")
    public RootEntity<List<LeavesResponseDTO>> getAllLeaves() {
        return ok(leavesService.getAllLeaves());
    }

    @GetMapping("/tc/{tc}")
    public RootEntity<List<LeavesResponseDTO>> getLeavesByTc(@PathVariable String tc) {
        return ok(leavesService.getLeavesByTc(tc));
    }

    @GetMapping("/{id}")
    public RootEntity<LeavesResponseDTO> getLeaveById(@PathVariable Long id) {
        return ok(leavesService.getLeaveById(id));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/delete/{id}")
    public RootEntity<String> deleteLeave(@PathVariable Long id) {
        leavesService.deleteLeave(id);
        return ok("İzin başarıyla silindi");
    }
    
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/approved")
    public RootEntity<List<LeavesResponseDTO>> getApprovedLeaves() {
        return ok(leavesService.getApprovedLeaves());
    }
    
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/approve/{id}/admin/{adminNo}")
    public RootEntity<LeavesResponseDTO> approveLeave(@PathVariable Long id, @PathVariable int adminNo) {
        return ok(leavesService.approveLeaveByAdmin(id, adminNo));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/reject/{id}")
    public RootEntity<LeavesResponseDTO> rejectLeave(@PathVariable Long id) {
        return ok(leavesService.rejectLeave(id));
    }
}
