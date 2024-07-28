package com.example.mvc_reg;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "users_account")
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String login;
    private String password;
    private String email;


}
