# Database Schema Documentation

## Entity Relationship Diagram (ERD)

```mermaid
erDiagram
    USERS ||--o{ CASES : "judges"
    USERS ||--o{ CASES : "lawyers"
    USERS ||--o{ CASES : "assigns"
    USERS ||--o{ CASE_FILES : "uploads"
    USERS ||--o{ CASE_REQUESTS : "initiates (lawyer)"
    USERS ||--o{ NOTIFICATIONS : "receives"
    
    CASES ||--o{ CASE_FILES : "contains"
    CASES ||--o{ CASE_REQUESTS : "has"
    CASES ||--|| MODEL_RESULTS : "has analysis"

    USERS {
        UUID id PK
        string first_name
        string last_name
        string email UK
        string national_id UK
        string password
        string role "ENUM(ADMIN, JUDGE, LAWYER)"
        string court
        boolean is_active
        timestamp created_at
    }

    CASES {
        UUID id PK
        string case_number
        string title
        string status "ENUM"
        UUID judge_id FK
        UUID lawyer_id FK
        UUID assigned_by_id FK
        timestamp created_at
    }

    CASE_FILES {
        UUID id PK
        UUID case_id FK
        UUID uploaded_by FK
        string file_name
        string file_url
        string file_type "ENUM"
    }

    CASE_REQUESTS {
        UUID id PK
        UUID case_id FK
        UUID lawyer_id FK
        string status "ENUM"
        timestamp requested_at
    }

    MODEL_RESULTS {
        UUID id PK
        UUID case_id FK
        string summary
        string judgment
        double confidence_score
    }

    NOTIFICATIONS {
        UUID id PK
        UUID recipient_id FK
        string title
        string message
        boolean is_read
    }