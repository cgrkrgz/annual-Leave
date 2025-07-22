package com.annualLeave.service;

import com.annualLeave.dto.LeavesRequestDTO;
import com.annualLeave.dto.LeavesResponseDTO;
import com.annualLeave.exception.BaseException;
import com.annualLeave.exception.ErrorMessage;
import com.annualLeave.exception.MessageType;
import com.annualLeave.model.Leaves;
import com.annualLeave.repository.AnnualLeaveLeavesRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class LeavesService {

    @Autowired
    private AnnualLeaveLeavesRepository leavesRepository;

    // İzin oluştur
    public LeavesResponseDTO createLeave(LeavesRequestDTO dto) {

        // 1. Tarih doğruluğunu kontrol et
        if (dto.getStartDate().isAfter(dto.getFinishDate())) {
            throw new BaseException(new ErrorMessage(MessageType.VALIDATION_EXCEPTION,
                    "Başlangıç tarihi, bitiş tarihinden sonra olamaz."));
        }

        // 2. Dönüştürme ve kayıt işlemleri
        Leaves leaves = new Leaves();
        BeanUtils.copyProperties(dto, leaves);
        //leaves.setApprovalStage(1); // başlangıçta 1. aşamadan başlasın
        
        leaves.setIsapprove(false);    // Yeni izin başta onaylı değil
        leaves.setIsnotapprove(false);
        
        Leaves saved = leavesRepository.save(leaves);

        LeavesResponseDTO responseDTO = new LeavesResponseDTO();
        BeanUtils.copyProperties(saved, responseDTO);
        return responseDTO;
    }


    // Tüm izinleri getir
    public List<LeavesResponseDTO> getAllLeaves() {
        List<Leaves> entityList = leavesRepository.findAll();
        List<LeavesResponseDTO> dtoList = new ArrayList<>();

        for (Leaves entity : entityList) {
            LeavesResponseDTO dto = new LeavesResponseDTO();
            BeanUtils.copyProperties(entity, dto);
            dtoList.add(dto);
        }
        return dtoList;
    }

    // TC’ye göre izinleri getir
    public List<LeavesResponseDTO> getLeavesByTc(String tc) {
        List<Leaves> entityList = leavesRepository.findByTc(tc);
        if (entityList.isEmpty()) {
            throw new BaseException(new ErrorMessage(MessageType.NO_RECORD_EXIST, "TC: " + tc));
        }
        List<LeavesResponseDTO> dtoList = new ArrayList<>();

        for (Leaves entity : entityList) {
            LeavesResponseDTO dto = new LeavesResponseDTO();
            BeanUtils.copyProperties(entity, dto);
            dtoList.add(dto);
        }
        return dtoList;
    }

    public LeavesResponseDTO getLeaveById(Long id) {
        Optional<Leaves> optional = leavesRepository.findById(id);
        if (optional.isPresent()) {
            LeavesResponseDTO dto = new LeavesResponseDTO();
            BeanUtils.copyProperties(optional.get(), dto);
            return dto;
        }
        // Hata fırlatıyoruz, kayıt yoksa
        throw new BaseException(new ErrorMessage(MessageType.NO_RECORD_EXIST, "İzin id: " + id + " bulunamadı"));
    }

    // İzin sil
    public void deleteLeave(Long id) {
        if (!leavesRepository.existsById(id)) {
            throw new BaseException(new ErrorMessage(MessageType.NO_RECORD_EXIST, "İzin ID: " + id));
        }
        leavesRepository.deleteById(id);
    }

    // Onaylanmış izinleri getir
    public List<LeavesResponseDTO> getApprovedLeaves() {
        List<Leaves> entityList = leavesRepository.findByIsapprove(true);
        List<LeavesResponseDTO> dtoList = new ArrayList<>();

        for (Leaves entity : entityList) {
            LeavesResponseDTO dto = new LeavesResponseDTO();
            BeanUtils.copyProperties(entity, dto);
            dtoList.add(dto);
        }
        return dtoList;
    }

    // Admin onayı verme
    public LeavesResponseDTO approveLeaveByAdmin(Long id, int adminNo) {
        Optional<Leaves> optionalLeave = leavesRepository.findById(id);

        if (optionalLeave.isEmpty()) {
            throw new BaseException(new ErrorMessage(MessageType.NO_RECORD_EXIST, "İzin id: " + id + " bulunamadı"));
        }

        Leaves leave = optionalLeave.get();

        switch (adminNo) {
            case 1 -> leave.setAdmin1(true);

            case 2 -> {
                if (!Boolean.TRUE.equals(leave.getAdmin1())) {
                    throw new BaseException(new ErrorMessage(MessageType.GENERAL_EXCEPTION, "Admin1 onayı olmadan admin2 onaylayamaz"));
                }
                leave.setAdmin2(true);
            }

            case 3 -> {
                if (!Boolean.TRUE.equals(leave.getAdmin2())) {
                    throw new BaseException(new ErrorMessage(MessageType.GENERAL_EXCEPTION, "Admin2 onayı olmadan admin3 onaylayamaz"));
                }
                leave.setAdmin3(true);
            }

            case 4 -> {
                if (!Boolean.TRUE.equals(leave.getAdmin3())) {
                    throw new BaseException(new ErrorMessage(MessageType.GENERAL_EXCEPTION, "Admin3 onayı olmadan admin4 onaylayamaz"));
                }
                leave.setAdmin4(true);
            }

            default -> throw new BaseException(new ErrorMessage(MessageType.GENERAL_EXCEPTION, "Geçersiz admin numarası: " + adminNo));
        }

        if (Boolean.TRUE.equals(leave.getAdmin1()) &&
            Boolean.TRUE.equals(leave.getAdmin2()) &&
            Boolean.TRUE.equals(leave.getAdmin3()) &&
            Boolean.TRUE.equals(leave.getAdmin4())) {
            leave.setIsapprove(true);
            leave.setIsnotapprove(false);
        }

        Leaves updatedLeave = leavesRepository.save(leave);

        LeavesResponseDTO dto = new LeavesResponseDTO();
        BeanUtils.copyProperties(updatedLeave, dto);
        return dto;
    }


    // İzin reddetme
    public LeavesResponseDTO rejectLeave(Long id) {
        Optional<Leaves> optionalLeave = leavesRepository.findById(id);

        if (optionalLeave.isEmpty()) {
            throw new BaseException(new ErrorMessage(MessageType.NO_RECORD_EXIST, "İzin id: " + id + " bulunamadı"));
        }

        Leaves leave = optionalLeave.get();

        leave.setIsapprove(false);
        leave.setIsnotapprove(true);

        Leaves updated = leavesRepository.save(leave);
        LeavesResponseDTO dto = new LeavesResponseDTO();
        BeanUtils.copyProperties(updated, dto);
        return dto;
    }


}
