package com.fullDetailed.fullDetailedDemo.domain.entities;

import com.fullDetailed.fullDetailedDemo.domain.enums.FileType;
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
@Table(name = "case_files")
public class CaseFile {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "case_id")
    private Case caseEntity;

    private String fileUrl;

    @Enumerated(EnumType.STRING)
    private FileType fileType;

    @ManyToOne
    @JoinColumn(name = "uploaded_by")
    private User uploadedBy;

    @CreationTimestamp
    private LocalDateTime uploadedAt;
}