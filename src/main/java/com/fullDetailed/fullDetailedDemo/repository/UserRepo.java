package com.fullDetailed.fullDetailedDemo.repository;

import java.util.Optional;
import java.util.UUID;

import aj.org.objectweb.asm.commons.Remapper;
import com.fullDetailed.fullDetailedDemo.domain.enums.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.fullDetailed.fullDetailedDemo.domain.entities.User;

public interface UserRepo extends JpaRepository<User,UUID>{
  Optional<User>findByEmail(String email);
    Page<User> findByRoleAndIsDeletedFalse(Role role, Pageable pageable);
  boolean existsByEmail(String email);
  Page<User> findAll(Pageable pageable);

  Page<User> findByRoleAndIsActiveFalseAndIsDeletedFalse(Role role, Pageable pageable);

  Page<User> findByRoleAndIsActiveTrueAndIsDeletedFalse(Role role, Pageable pageable);
}
