package com.fullDetailed.fullDetailedDemo.repository;

import com.fullDetailed.fullDetailedDemo.domain.entities.Case;
import com.fullDetailed.fullDetailedDemo.domain.entities.User;
import com.fullDetailed.fullDetailedDemo.domain.enums.AssignStatus;
import com.fullDetailed.fullDetailedDemo.domain.enums.CaseStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

public interface CaseRepository extends JpaRepository<Case, UUID> {

            Optional<Case> findByCaseNumberAndIsDeletedFalse(String caseNumber);

    Page<Case>findByJudge(User user, Pageable pageable);
    boolean existsByCaseNumber(String caseNumber);
    Page<Case> findByIsDeletedFalse(Pageable pageable);
    Page<Case> findByIsDeletedTrue(Pageable pageable);
    Page<Case> findByStatusAndIsDeletedFalse(CaseStatus status, Pageable pageable);
    Page<Case> findByJudgeIsNotNullAndLawyerIsNotNullAndIsDeletedFalse(Pageable pageable);
    Page<Case> findByJudgeAndStatusAndIsDeletedFalse(User user, CaseStatus status, Pageable pageable);
    Page<Case> findByJudgeAndCreatedAtBetweenAndIsDeletedFalse(
            User user,
            LocalDateTime startDate,
            LocalDateTime endDate,
            Pageable pageable
    );


    Page<Case>findByLawyer(User user, Pageable pageable);
    Page<Case> findByLawyerIsNotNullAndLawyerIsNotNullAndIsDeletedFalse(Pageable pageable);
    Page<Case> findByLawyerAndStatusAndIsDeletedFalse(User user, CaseStatus status, Pageable pageable);
    Page<Case> findByLawyerAndCreatedAtBetweenAndIsDeletedFalse(
            User user,
            LocalDateTime startDate,
            LocalDateTime endDate,
            Pageable pageable
    );
    Optional<Case> findByCaseNumber(String caseNumber);
    Page<Case> findByAssignStatusAndIsDeletedFalse(Pageable pageable, AssignStatus status);
    long countByAssignStatusAndIsDeletedFalse(AssignStatus status);
}
