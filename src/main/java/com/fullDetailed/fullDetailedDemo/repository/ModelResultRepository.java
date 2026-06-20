package com.fullDetailed.fullDetailedDemo.repository;

import com.fullDetailed.fullDetailedDemo.domain.entities.ModelResult;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ModelResultRepository extends JpaRepository<ModelResult, UUID> {

  boolean existsByCaseEntity_Id(UUID caseId);
  Optional <ModelResult> findByCaseEntity_id(UUID caseId);

}