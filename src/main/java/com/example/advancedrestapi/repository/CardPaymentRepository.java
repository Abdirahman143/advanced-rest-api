package com.example.advancedrestapi.repository;

import com.example.advancedrestapi.model.CardPayment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CardPaymentRepository extends JpaRepository<CardPayment,Long> {
}
