package com.fullDetailed.fullDetailedDemo.domain.entities;

import com.fullDetailed.fullDetailedDemo.domain.enums.AssignStatus;
import com.fullDetailed.fullDetailedDemo.domain.enums.CaseStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "cases")
public class Case {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String caseNumber;

    @Column(nullable = false)
    private String title;

    @Column(length = 2000)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CaseStatus status;

    @Enumerated(EnumType.STRING)
    @Column(nullable = true)
    private AssignStatus assignStatus;

    @ManyToOne
    @JoinColumn(name = "judge_id")
    private User judge;

    @ManyToOne
    @JoinColumn(name = "lawyer_id")
    private User lawyer;

    @ManyToOne
    @JoinColumn(name = "assigned_by_id")
    private User assignedBy;

    @Column(name = "is_deleted", nullable = false)
    private boolean isDeleted = false;

    @OneToMany(mappedBy = "caseEntity", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<CaseFile> files;

    @Column(name = "court_ruling")
    private String courtRuling;

    private String modelSummary;
    private String modelJudgment;
    private String finalDecision;

    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}
