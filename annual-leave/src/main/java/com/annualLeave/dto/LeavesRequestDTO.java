package com.annualLeave.dto;

import com.annualLeave.model.FileInfo;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDate;

@Data
public class LeavesRequestDTO {

    private Long id;

    @NotBlank(message = "Şirket alanı boş olamaz")
    private String company;

    @NotBlank(message = "Pozisyon alanı boş olamaz")
    private String position;

    @NotBlank(message = "TC alanı boş olamaz")
    @Size(min = 11, max = 11, message = "TC 11 haneli olmalıdır")
    private String tc;

    @NotBlank(message = "İsim boş olamaz")
    private String name;

    @NotBlank(message = "Soyisim boş olamaz")
    private String surname;

    @NotNull(message = "Başlangıç tarihi boş olamaz")
    private LocalDate startDate;

    @NotNull(message = "Bitiş tarihi boş olamaz")
    private LocalDate finishDate;

    private LocalDate currentDate;

    @Min(value = 1, message = "Toplam izin günü en az 1 olmalıdır")
    private Integer totalDate;

    @NotBlank(message = "İzin türü boş olamaz")
    private String leavesType;

    private String comment;

    private FileInfo file;

    private Boolean isHalfDay;
}
