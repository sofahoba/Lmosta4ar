package com.fullDetailed.fullDetailedDemo.repository;

import com.fullDetailed.fullDetailedDemo.domain.entities.Case;
import com.fullDetailed.fullDetailedDemo.domain.entities.CaseRequests;
import com.fullDetailed.fullDetailedDemo.domain.entities.User;
import com.fullDetailed.fullDetailedDemo.domain.enums.RequestStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface CaseRequestRepository extends JpaRepository<CaseRequests, UUID> {
    boolean existsByLawyerAndLegalCase(User lawyer, Case legalCase);
    Page<CaseRequests> findByStatus(RequestStatus status, Pageable pageable);
}