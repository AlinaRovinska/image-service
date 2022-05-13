package com.project.imageservice.dao;

import com.project.imageservice.domain.Role;
import com.project.imageservice.domain.enums.RoleNames;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Integer> {

    Optional<Role> findByRole (RoleNames roleNames);
}
