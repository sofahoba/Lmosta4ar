package com.fullDetailed.fullDetailedDemo.repository;

import com.fullDetailed.fullDetailedDemo.domain.entities.Case;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CaseRepository extends JpaRepository<Case, UUID> {
}
