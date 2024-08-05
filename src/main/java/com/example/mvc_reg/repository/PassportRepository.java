package com.example.mvc_reg.repository;

import com.example.mvc_reg.entity.Passport;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PassportRepository extends JpaRepository<Passport, Integer> {
}
