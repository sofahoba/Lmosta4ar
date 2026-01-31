package com.fullDetailed.fullDetailedDemo.repository;

import com.fullDetailed.fullDetailedDemo.domain.entities.Case;
import com.fullDetailed.fullDetailedDemo.domain.entities.User;
import com.fullDetailed.fullDetailedDemo.domain.enums.CaseStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.UUID;

public interface CaseRepository extends JpaRepository<Case, UUID> {
    Page<Case>findByJudge(User judge, Pageable pageable);
    boolean existsByCaseNumber(String caseNumber);
    Page<Case> findByIsDeletedFalse(Pageable pageable);
    Page<Case> findByIsDeletedTrue(Pageable pageable);
    Page<Case> findByStatusAndIsDeletedFalse(CaseStatus status, Pageable pageable);
    Page<Case> findByJudgeIsNotNullAndLawyerIsNotNullAndIsDeletedFalse(Pageable pageable);
    Page<Case> findByJudgeAndStatusAndIsDeletedFalse(User judge, CaseStatus status, Pageable pageable);
    Page<Case> findByJudgeAndCreatedAtBetweenAndIsDeletedFalse(
            User judge,
            LocalDateTime startDate,
            LocalDateTime endDate,
            Pageable pageable
    );
}
