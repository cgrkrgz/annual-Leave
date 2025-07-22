package com.annualLeave.model;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FileInfo {

    @Column(name = "file_id", unique = true)
    private String id;

    @Column(name = "file_name")
    private String name;

    @Column(name = "file_type")
    private String type;
}

