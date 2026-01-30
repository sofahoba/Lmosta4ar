package com.fullDetailed.fullDetailedDemo.repository;

import com.fullDetailed.fullDetailedDemo.domain.entities.CaseFile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CaseFileRepository extends JpaRepository<CaseFile, UUID> {
}
