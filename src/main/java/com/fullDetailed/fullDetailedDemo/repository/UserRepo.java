package com.fullDetailed.fullDetailedDemo.repository;

import java.util.Optional;
import java.util.UUID;

import aj.org.objectweb.asm.commons.Remapper;
import com.fullDetailed.fullDetailedDemo.domain.enums.Role;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.fullDetailed.fullDetailedDemo.domain.entities.User;
import org.springframework.data.jpa.repository.Query;

public interface UserRepo extends JpaRepository<User,UUID>{

    @Query(value = "SELECT * FROM users u WHERE u.email = :email LIMIT 1", nativeQuery = true)
    Optional<User>findByEmail(String email);
    Page<User> findByRoleAndIsDeletedFalse(Role role, Pageable pageable);
  boolean existsByEmail(String email);
  Page<User> findAllByIsDeletedFalse(Pageable pageable);

  Page<User> findByRoleAndIsActiveFalseAndIsDeletedFalse(Role role, Pageable pageable);

  Page<User> findByRoleAndIsActiveTrueAndIsDeletedFalse(Role role, Pageable pageable);

  Page<User> findByRoleAndIsApprovedTrueAndIsDeletedFalse(Role role, Pageable pageable);

  Page<User> findByRoleAndIsApprovedFalseAndIsDeletedFalse(Role role, Pageable pageable);

  boolean existsByNationalId(String nationalId);
}
