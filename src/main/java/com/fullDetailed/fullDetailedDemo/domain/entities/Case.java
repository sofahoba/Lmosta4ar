package com.fullDetailed.fullDetailedDemo.domain.entities;

import com.fullDetailed.fullDetailedDemo.domain.enums.CaseStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime;
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

    @ManyToOne
    @JoinColumn(name = "judge_id")
    private User judge;

    @ManyToOne
    @JoinColumn(name = "assigned_by_id")
    private User assignedBy;


    @OneToMany(mappedBy = "caseEntity", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<CaseFile> files;

    private String modelSummary;
    private String modelJudgment;

    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}
