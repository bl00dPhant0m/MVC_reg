package com.example.mvc_reg.entity;

import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;


import java.time.LocalDate;

@Entity
@Table(name = "users_account")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "логин не может быть пустым")
    @Size(min = 4, max = 20, message = "логин должен быть от 4 до 20 символов")
    private String login;

    @Size(min = 6, max = 16, message = "пароль должен быть от 6 до 16 символов")
    private String password;

    @Email
    private String email;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate dateOfRegistration = LocalDate.now();


    @Valid
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private Passport passport;


}
