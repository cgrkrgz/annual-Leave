package com.annualLeave.model;

import jakarta.persistence.*;
import lombok.*;


import java.time.LocalDate;


@Entity
@Table(name = "leaves")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Leaves {

	@Id
    @Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // UUID yerine String


    @Column(name = "company", nullable = false)
    private String company;

    @Column(name = "position", nullable = false)
    private String position;

    @Column(name = "tc", nullable = false)
    private String tc;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "surname", nullable = false)
    private String surname;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "finish_date", nullable = false)
    private LocalDate finishDate;

    @Column(name = "current_date_field")
    private LocalDate currentDate;

    @Column(name = "total_date")
    private Integer totalDate;

    @Column(name = "leaves_type", nullable = false)
    private String leavesType;

    @Column(name = "comment")
    private String comment;

    @Embedded
    private FileInfo file;

    @Column(name = "admin1")
    private Boolean admin1 = false;

    @Column(name = "admin2")
    private Boolean admin2 = false;

    @Column(name = "admin3")
    private Boolean admin3 = false;

    @Column(name = "admin4")
    private Boolean admin4 = false;

    @Column(name = "isapprove")
    private Boolean isapprove = true;

    @Column(name = "isnotapprove")
    private Boolean isnotapprove = false;

    @Column(name = "is_half_day")
    private Boolean isHalfDay = false;
}
