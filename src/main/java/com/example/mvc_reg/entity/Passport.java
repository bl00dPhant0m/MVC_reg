package com.example.mvc_reg.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

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

    @NotBlank
    private String name;

    @NotBlank
    private String surname;

    @Min(value = 1000, message = "серия должна быть больше 1000")
    @Max(value = 9999, message = "серия должна быть меньше 9999")
    private int serial;

    @Min(value = 100_000, message = "номер должна быть больше 100_000")
    @Max(value = 999_999, message = "номер должна быть больше 999_999")
    private int number;

    @NotNull(message = "Дата выдачи не может быть пустой")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate dateOfIssue;

    @NotNull(message = "Дата рождения не может быть пустой")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate dateOfBirth;

    @OneToOne
    @JoinColumn(name = "user_id")
    @ToString.Exclude
    private User user;
}
