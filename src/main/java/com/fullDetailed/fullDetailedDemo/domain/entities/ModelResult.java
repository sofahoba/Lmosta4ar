package com.fullDetailed.fullDetailedDemo.domain.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "model_results")
public class ModelResult {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @OneToOne
    @JoinColumn(name = "case_id")
    private Case caseEntity;

    @Column(length = 5000)
    private String summary;

    @Column(length = 2000)
    private String judgment;

    private double confidenceScore;

    @CreationTimestamp
    private LocalDateTime createdAt;
}
