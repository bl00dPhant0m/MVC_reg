package com.example.mvc_reg.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "passports")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Passport {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    private String surname;
    private int serial;
    private int number;
    private LocalDate dateOfIssue;
    private LocalDate dateOfBirth;
}
