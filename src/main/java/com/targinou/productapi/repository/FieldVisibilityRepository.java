package com.targinou.productapi.repository;

import com.targinou.productapi.model.FieldVisibility;
import com.targinou.productapi.model.enums.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FieldVisibilityRepository extends JpaRepository<FieldVisibility, Long> {
    Optional<FieldVisibility> findByRole(Role role);
}
