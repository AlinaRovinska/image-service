package com.project.imageservice.dao;

import com.project.imageservice.domain.Account;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Integer> {

    @Query("delete from Account a where a.id = :accountId")
    @Modifying
    void deleteById(Integer accountId);

    @EntityGraph(attributePaths = {"roles"})
    Optional<Account> findByUserName(String userName);
}
