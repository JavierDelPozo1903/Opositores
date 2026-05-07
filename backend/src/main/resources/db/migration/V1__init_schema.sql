-- =============================================================
-- opositOS — Esquema completo de base de datos
-- MariaDB / MySQL compatible
-- =============================================================

SET FOREIGN_KEY_CHECKS = 0;

-- -------------------------------------------------------------
-- USERS
-- -------------------------------------------------------------
CREATE TABLE IF NOT EXISTS users (
    id                  BIGINT          NOT NULL AUTO_INCREMENT,
    email               VARCHAR(255)    NOT NULL,
    password_hash       VARCHAR(255)    NOT NULL,
    name                VARCHAR(255)    NOT NULL,
    subscription_plan   ENUM('FREE','PRO')                          NOT NULL DEFAULT 'FREE',
    subscription_status ENUM('ACTIVE','PAST_DUE','CANCELED')        NOT NULL DEFAULT 'ACTIVE',
    created_at          DATETIME(6)     NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    updated_at          DATETIME(6)     NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),
    PRIMARY KEY (id),
    UNIQUE KEY uq_users_email (email)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- -------------------------------------------------------------
-- OPPOSITIONS
-- -------------------------------------------------------------
CREATE TABLE IF NOT EXISTS oppositions (
    id               BIGINT          NOT NULL AUTO_INCREMENT,
    user_id          BIGINT          NOT NULL,
    name             VARCHAR(255)    NOT NULL,
    scope            ENUM('AGE','CCAA','LOCAL','JUSTICIA','EDUCACION','SANIDAD','POLICIA','OTROS'),
    target_exam_date DATE,
    hours_per_week   DOUBLE,
    created_at       DATETIME(6)     NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    updated_at       DATETIME(6)     NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),
    PRIMARY KEY (id),
    CONSTRAINT fk_oppositions_user FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- -------------------------------------------------------------
-- BLOCKS
-- -------------------------------------------------------------
CREATE TABLE IF NOT EXISTS blocks (
    id            BIGINT          NOT NULL AUTO_INCREMENT,
    opposition_id BIGINT          NOT NULL,
    name          VARCHAR(255)    NOT NULL,
    weight        DOUBLE          NOT NULL DEFAULT 1.0,
    PRIMARY KEY (id),
    CONSTRAINT fk_blocks_opposition FOREIGN KEY (opposition_id) REFERENCES oppositions (id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- -------------------------------------------------------------
-- TOPICS
-- -------------------------------------------------------------
CREATE TABLE IF NOT EXISTS topics (
    id               BIGINT          NOT NULL AUTO_INCREMENT,
    block_id         BIGINT          NOT NULL,
    official_number  VARCHAR(50),
    title            VARCHAR(500)    NOT NULL,
    difficulty       INT             NOT NULL DEFAULT 3,
    priority         INT             NOT NULL DEFAULT 3,
    status           ENUM('NOT_STARTED','STUDYING','REVIEWED','MASTERED') NOT NULL DEFAULT 'NOT_STARTED',
    review_count     INT             NOT NULL DEFAULT 0,
    last_review_date DATE,
    PRIMARY KEY (id),
    CONSTRAINT fk_topics_block FOREIGN KEY (block_id) REFERENCES blocks (id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- -------------------------------------------------------------
-- DOCUMENTS
-- -------------------------------------------------------------
CREATE TABLE IF NOT EXISTS documents (
    id         BIGINT          NOT NULL AUTO_INCREMENT,
    topic_id   BIGINT          NOT NULL,
    user_id    BIGINT          NOT NULL,
    name       VARCHAR(255)    NOT NULL,
    file_url   VARCHAR(1000)   NOT NULL,
    type       ENUM('PDF','TEXT','URL') NOT NULL,
    language   VARCHAR(10)     NOT NULL DEFAULT 'es',
    created_at DATETIME(6)     NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    PRIMARY KEY (id),
    CONSTRAINT fk_documents_topic FOREIGN KEY (topic_id) REFERENCES topics (id) ON DELETE CASCADE,
    CONSTRAINT fk_documents_user  FOREIGN KEY (user_id)  REFERENCES users (id)  ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- -------------------------------------------------------------
-- FLASHCARDS
-- -------------------------------------------------------------
CREATE TABLE IF NOT EXISTS flashcards (
    id               BIGINT          NOT NULL AUTO_INCREMENT,
    topic_id         BIGINT          NOT NULL,
    question         TEXT            NOT NULL,
    answer           TEXT            NOT NULL,
    next_review_date DATE,
    interval_days    INT             NOT NULL DEFAULT 1,
    ease_factor      DOUBLE          NOT NULL DEFAULT 2.5,
    PRIMARY KEY (id),
    CONSTRAINT fk_flashcards_topic FOREIGN KEY (topic_id) REFERENCES topics (id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- -------------------------------------------------------------
-- QUESTIONS
-- -------------------------------------------------------------
CREATE TABLE IF NOT EXISTS questions (
    id              BIGINT          NOT NULL AUTO_INCREMENT,
    topic_id        BIGINT          NOT NULL,
    source          ENUM('USER','AI')               NOT NULL DEFAULT 'AI',
    type            ENUM('MCQ','OPEN','TRUE_FALSE')  NOT NULL DEFAULT 'MCQ',
    question_text   TEXT            NOT NULL,
    correct_answer  VARCHAR(1000)   NOT NULL,
    options         VARCHAR(2000),
    PRIMARY KEY (id),
    CONSTRAINT fk_questions_topic FOREIGN KEY (topic_id) REFERENCES topics (id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- -------------------------------------------------------------
-- TESTS
-- -------------------------------------------------------------
CREATE TABLE IF NOT EXISTS tests (
    id                  BIGINT          NOT NULL AUTO_INCREMENT,
    user_id             BIGINT          NOT NULL,
    opposition_id       BIGINT          NOT NULL,
    mode                ENUM('PRACTICE','EXAM_SIMULATION') NOT NULL DEFAULT 'PRACTICE',
    num_questions       INT,
    time_limit_minutes  INT,
    created_at          DATETIME(6)     NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    PRIMARY KEY (id),
    CONSTRAINT fk_tests_user       FOREIGN KEY (user_id)       REFERENCES users       (id) ON DELETE CASCADE,
    CONSTRAINT fk_tests_opposition FOREIGN KEY (opposition_id) REFERENCES oppositions (id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- -------------------------------------------------------------
-- TEST_QUESTIONS
-- -------------------------------------------------------------
CREATE TABLE IF NOT EXISTS test_questions (
    id          BIGINT  NOT NULL AUTO_INCREMENT,
    test_id     BIGINT  NOT NULL,
    question_id BIGINT  NOT NULL,
    order_index INT,
    PRIMARY KEY (id),
    CONSTRAINT fk_tq_test     FOREIGN KEY (test_id)     REFERENCES tests     (id) ON DELETE CASCADE,
    CONSTRAINT fk_tq_question FOREIGN KEY (question_id) REFERENCES questions (id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- -------------------------------------------------------------
-- TEST_RESULTS
-- -------------------------------------------------------------
CREATE TABLE IF NOT EXISTS test_results (
    id              BIGINT          NOT NULL AUTO_INCREMENT,
    test_id         BIGINT          NOT NULL,
    user_id         BIGINT          NOT NULL,
    start_time      DATETIME(6),
    end_time        DATETIME(6),
    score           DOUBLE,
    correct_count   INT,
    incorrect_count INT,
    blank_count     INT,
    PRIMARY KEY (id),
    UNIQUE KEY uq_test_results_test (test_id),
    CONSTRAINT fk_tr_test FOREIGN KEY (test_id) REFERENCES tests (id) ON DELETE CASCADE,
    CONSTRAINT fk_tr_user FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- -------------------------------------------------------------
-- TEST_ANSWERS
-- -------------------------------------------------------------
CREATE TABLE IF NOT EXISTS test_answers (
    id             BIGINT          NOT NULL AUTO_INCREMENT,
    test_result_id BIGINT          NOT NULL,
    question_id    BIGINT          NOT NULL,
    selected_option VARCHAR(1000),
    is_correct     TINYINT(1),
    PRIMARY KEY (id),
    CONSTRAINT fk_ta_test_result FOREIGN KEY (test_result_id) REFERENCES test_results (id) ON DELETE CASCADE,
    CONSTRAINT fk_ta_question    FOREIGN KEY (question_id)    REFERENCES questions     (id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- -------------------------------------------------------------
-- STUDY_SESSION_PLANS
-- -------------------------------------------------------------
CREATE TABLE IF NOT EXISTS study_session_plans (
    id               BIGINT  NOT NULL AUTO_INCREMENT,
    opposition_id    BIGINT  NOT NULL,
    topic_id         BIGINT,
    date             DATE    NOT NULL,
    planned_minutes  INT,
    status           ENUM('PENDING','COMPLETED','SKIPPED') NOT NULL DEFAULT 'PENDING',
    PRIMARY KEY (id),
    CONSTRAINT fk_ssp_opposition FOREIGN KEY (opposition_id) REFERENCES oppositions (id) ON DELETE CASCADE,
    CONSTRAINT fk_ssp_topic      FOREIGN KEY (topic_id)      REFERENCES topics      (id) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- -------------------------------------------------------------
-- STUDY_SESSION_LOGS
-- -------------------------------------------------------------
CREATE TABLE IF NOT EXISTS study_session_logs (
    id            BIGINT          NOT NULL AUTO_INCREMENT,
    user_id       BIGINT          NOT NULL,
    opposition_id BIGINT          NOT NULL,
    date          DATE            NOT NULL,
    minutes       INT,
    notes         VARCHAR(1000),
    PRIMARY KEY (id),
    CONSTRAINT fk_ssl_user       FOREIGN KEY (user_id)       REFERENCES users       (id) ON DELETE CASCADE,
    CONSTRAINT fk_ssl_opposition FOREIGN KEY (opposition_id) REFERENCES oppositions (id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- -------------------------------------------------------------
-- AI_REQUESTS
-- -------------------------------------------------------------
CREATE TABLE IF NOT EXISTS ai_requests (
    id           BIGINT          NOT NULL AUTO_INCREMENT,
    user_id      BIGINT          NOT NULL,
    document_id  BIGINT,
    request_type ENUM('SUMMARY','FLASHCARDS','MCQ','ESSAY_QUESTIONS') NOT NULL,
    tokens_used  INT,
    status       ENUM('PENDING','SUCCESS','ERROR') NOT NULL DEFAULT 'PENDING',
    created_at   DATETIME(6)     NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    PRIMARY KEY (id),
    CONSTRAINT fk_air_user     FOREIGN KEY (user_id)     REFERENCES users      (id) ON DELETE CASCADE,
    CONSTRAINT fk_air_document FOREIGN KEY (document_id) REFERENCES documents  (id) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- -------------------------------------------------------------
-- NOTIFICATIONS
-- -------------------------------------------------------------
CREATE TABLE IF NOT EXISTS notifications (
    id           BIGINT          NOT NULL AUTO_INCREMENT,
    user_id      BIGINT          NOT NULL,
    type         ENUM('STUDY_REMINDER','REVIEW_REMINDER','EXAM_REMINDER') NOT NULL,
    scheduled_at DATETIME(6)     NOT NULL,
    sent_at      DATETIME(6),
    PRIMARY KEY (id),
    CONSTRAINT fk_notif_user FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

SET FOREIGN_KEY_CHECKS = 1;
