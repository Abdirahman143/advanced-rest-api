package com.example.advancedrestapi.repository;

import com.example.advancedrestapi.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

    @Query("SELECT a FROM Account a WHERE a.accountNumber = ?1 AND a.email = ?2")
    Optional<Account> findAccountDetails(String accountNumber, String email);
}