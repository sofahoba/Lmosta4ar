package com.fullDetailed.fullDetailedDemo.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fullDetailed.fullDetailedDemo.domain.entities.User;

public interface UserRepo extends JpaRepository<User,UUID>{
  Optional<User>findByEmail(String email);

  boolean existsByEmail(String email);
}
