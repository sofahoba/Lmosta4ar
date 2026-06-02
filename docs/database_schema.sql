-- ============================================================
-- Lmosta4ar (المستشار) — Database Schema
-- PostgreSQL 16
-- Generated from JPA Entity definitions
-- ============================================================

-- ============================================================
-- ENUM TYPES
-- ============================================================

CREATE TYPE role_enum AS ENUM ('ADMIN', 'JUDGE', 'LAWYER', 'EMPTY');
CREATE TYPE case_status_enum AS ENUM ('PENDING', 'UNDER_DISCUSSION', 'COMPLETED');
CREATE TYPE assign_status_enum AS ENUM ('FULLY_ASSIGNED', 'ASSIGNED_TO_LAWYER', 'ASSIGNED_TO_JUDGE');
CREATE TYPE request_status_enum AS ENUM ('PENDING', 'APPROVED', 'REJECTED');
CREATE TYPE file_type_enum AS ENUM ('PDF', 'DOCUMENT', 'IMAGE', 'OTHER');


-- ============================================================
-- TABLE: users
-- ============================================================

CREATE TABLE users (
    id                  UUID            PRIMARY KEY DEFAULT gen_random_uuid(),
    first_name          VARCHAR(255)    NOT NULL,
    last_name           VARCHAR(255)    NOT NULL,
    password            VARCHAR(255)    NOT NULL,
    email               VARCHAR(255)    NOT NULL UNIQUE,
    national_id         VARCHAR(255)    UNIQUE,
    age                 INTEGER,
    role                VARCHAR(20)     NOT NULL,   -- ADMIN | JUDGE | LAWYER | EMPTY
    assigned_cases_count INTEGER        DEFAULT 0,
    is_active           BOOLEAN         DEFAULT TRUE,
    is_deleted          BOOLEAN         DEFAULT FALSE,
    "passwordReseted"   BOOLEAN         DEFAULT FALSE,
    "isApproved"        BOOLEAN         DEFAULT FALSE,
    otp_code            VARCHAR(255),
    "Otp_expiration_time" TIMESTAMP,
    court               VARCHAR(255),
    created_at          TIMESTAMP       DEFAULT CURRENT_TIMESTAMP,
    updated_at          TIMESTAMP       DEFAULT CURRENT_TIMESTAMP
);

CREATE UNIQUE INDEX idx_user_email ON users (email);


-- ============================================================
-- TABLE: cases
-- ============================================================

CREATE TABLE cases (
    id                  UUID            PRIMARY KEY DEFAULT gen_random_uuid(),
    case_number         VARCHAR(255)    NOT NULL,
    title               VARCHAR(255)    NOT NULL,
    description         VARCHAR(2000),
    status              VARCHAR(30)     NOT NULL,   -- PENDING | UNDER_DISCUSSION | COMPLETED
    assign_status       VARCHAR(30),                -- FULLY_ASSIGNED | ASSIGNED_TO_LAWYER | ASSIGNED_TO_JUDGE
    judge_id            UUID            REFERENCES users(id) ON DELETE SET NULL,
    lawyer_id           UUID            REFERENCES users(id) ON DELETE SET NULL,
    assigned_by_id      UUID            REFERENCES users(id) ON DELETE SET NULL,
    is_deleted          BOOLEAN         NOT NULL DEFAULT FALSE,
    court_ruling        VARCHAR(255),
    model_summary       TEXT,
    model_judgment      TEXT,
    final_decision      TEXT,
    created_at          TIMESTAMP       DEFAULT CURRENT_TIMESTAMP
);


-- ============================================================
-- TABLE: case_files
-- ============================================================

CREATE TABLE case_files (
    id                  UUID            PRIMARY KEY DEFAULT gen_random_uuid(),
    case_id             UUID            REFERENCES cases(id) ON DELETE CASCADE,
    file_name           VARCHAR(255)    NOT NULL,
    original_file_name  VARCHAR(255)    NOT NULL,
    file_url            VARCHAR(255),
    file_type           VARCHAR(20),    -- PDF | DOCUMENT | IMAGE | OTHER
    uploaded_by         UUID            REFERENCES users(id) ON DELETE SET NULL,
    uploaded_at         TIMESTAMP       DEFAULT CURRENT_TIMESTAMP
);


-- ============================================================
-- TABLE: case_requests
-- ============================================================

CREATE TABLE case_requests (
    id                  UUID            PRIMARY KEY DEFAULT gen_random_uuid(),
    lawyer_id           UUID            NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    case_id             UUID            NOT NULL REFERENCES cases(id) ON DELETE CASCADE,
    status              VARCHAR(20)     NOT NULL DEFAULT 'PENDING',  -- PENDING | APPROVED | REJECTED
    created_at          TIMESTAMP       DEFAULT CURRENT_TIMESTAMP
);


-- ============================================================
-- TABLE: model_results  (AI Analysis Output)
-- ============================================================

CREATE TABLE model_results (
    id                          UUID            PRIMARY KEY DEFAULT gen_random_uuid(),
    case_id                     UUID            NOT NULL UNIQUE REFERENCES cases(id) ON DELETE CASCADE,
    summary                     TEXT,
    raw_response                TEXT,
    defendants                  TEXT,           -- JSON array of DefendantDto
    charges                     TEXT,           -- JSON array of ChargeDto
    incidents                   TEXT,           -- JSON array of IncidentDto
    evidences                   TEXT,           -- JSON array of EvidenceDto
    witness_statements          TEXT,           -- JSON array of WitnessStatementDto
    confessions                 TEXT,           -- JSON array of ConfessionDto
    lab_reports                 TEXT,           -- JSON array of LabReportDto
    criminal_proceedings        TEXT,           -- JSON array of CriminalProceedingDto
    defense_documents           TEXT,           -- JSON array of DefenseDocumentDto
    procedural_audit            TEXT,           -- JSON object ProceduralAuditDto
    suggested_verdict           TEXT,           -- JSON object SuggestedVerdictDto
    court                       TEXT,
    court_level                 TEXT,
    jurisdiction                TEXT,
    prosecutor_name             TEXT,
    completed_agents            TEXT,
    processing_errors           TEXT,
    has_procedural_violations   BOOLEAN,
    defendant_count             INTEGER,
    charge_count                INTEGER,
    confidence_score            DOUBLE PRECISION,
    created_at                  TIMESTAMP       DEFAULT CURRENT_TIMESTAMP
);


-- ============================================================
-- TABLE: notifications
-- ============================================================

CREATE TABLE notifications (
    id                  UUID            PRIMARY KEY DEFAULT gen_random_uuid(),
    title               VARCHAR(255)    NOT NULL,
    message             VARCHAR(255)    NOT NULL,
    is_read             BOOLEAN         NOT NULL DEFAULT FALSE,
    recipient_id        UUID            NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    created_at          TIMESTAMP       DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_notification_recipient ON notifications (recipient_id);
CREATE INDEX idx_notification_recipient_read ON notifications (recipient_id, is_read);


-- ============================================================
-- Spring Batch metadata tables (auto-created by Spring Batch)
-- These tables are created by: spring.batch.jdbc.initialize-schema=always
-- ============================================================
-- BATCH_JOB_INSTANCE
-- BATCH_JOB_EXECUTION
-- BATCH_JOB_EXECUTION_PARAMS
-- BATCH_JOB_EXECUTION_CONTEXT
-- BATCH_STEP_EXECUTION
-- BATCH_STEP_EXECUTION_CONTEXT
