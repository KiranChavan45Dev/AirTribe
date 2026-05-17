-- =========================================================
-- CHRONOS - DISTRIBUTED JOB SCHEDULER
-- Initial Database Schema
-- =========================================================

-- =========================================================
-- EXTENSIONS
-- =========================================================

CREATE EXTENSION IF NOT EXISTS "pgcrypto";

-- =========================================================
-- USERS TABLE
-- =========================================================

CREATE TABLE users (
                       id UUID PRIMARY KEY DEFAULT gen_random_uuid(),

                       username VARCHAR(100) UNIQUE NOT NULL,

                       email VARCHAR(255) UNIQUE NOT NULL,

                       password_hash VARCHAR(255) NOT NULL,

                       role VARCHAR(50) NOT NULL DEFAULT 'USER',

                       created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- =========================================================
-- JOB STATUS ENUM VALUES (DOCUMENTATION)
-- =========================================================
--
-- PENDING
-- SCHEDULED
-- RUNNING
-- SUCCESS
-- FAILED
-- RETRYING
-- CANCELLED
-- DEAD
--
-- =========================================================

-- =========================================================
-- SCHEDULE TYPES (DOCUMENTATION)
-- =========================================================
--
-- ONCE
-- CRON
-- INTERVAL
--
-- =========================================================

-- =========================================================
-- JOBS TABLE
-- =========================================================

CREATE TABLE jobs (
                      id UUID PRIMARY KEY DEFAULT gen_random_uuid(),

                      user_id UUID NOT NULL,

                      job_name VARCHAR(255) NOT NULL,

                      job_type VARCHAR(100) NOT NULL,

                      payload JSONB,

                      status VARCHAR(50) NOT NULL DEFAULT 'PENDING',

                      schedule_type VARCHAR(20) NOT NULL,

                      cron_expression VARCHAR(100),

                      run_at TIMESTAMP,

                      next_run_at TIMESTAMP NOT NULL,

                      retry_count INT NOT NULL DEFAULT 0,

                      max_retries INT NOT NULL DEFAULT 3,

                      priority INT NOT NULL DEFAULT 1,

                      last_error TEXT,

                      is_recurring BOOLEAN NOT NULL DEFAULT FALSE,

                      created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

                      updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

                      CONSTRAINT fk_jobs_user
                          FOREIGN KEY (user_id)
                              REFERENCES users(id)
                              ON DELETE CASCADE
);

-- =========================================================
-- JOB EXECUTION LOGS
-- =========================================================

CREATE TABLE job_execution_logs (
                                    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),

                                    job_id UUID NOT NULL,

                                    execution_number INT NOT NULL,

                                    status VARCHAR(50) NOT NULL,

                                    started_at TIMESTAMP,

                                    completed_at TIMESTAMP,

                                    execution_time_ms BIGINT,

                                    error_message TEXT,

                                    worker_instance VARCHAR(255),

                                    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

                                    CONSTRAINT fk_execution_job
                                        FOREIGN KEY (job_id)
                                            REFERENCES jobs(id)
                                            ON DELETE CASCADE
);

-- =========================================================
-- NOTIFICATIONS TABLE
-- =========================================================

CREATE TABLE notifications (
                               id UUID PRIMARY KEY DEFAULT gen_random_uuid(),

                               user_id UUID NOT NULL,

                               job_id UUID,

                               type VARCHAR(50) NOT NULL,

                               message TEXT NOT NULL,

                               is_read BOOLEAN NOT NULL DEFAULT FALSE,

                               created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

                               CONSTRAINT fk_notifications_user
                                   FOREIGN KEY (user_id)
                                       REFERENCES users(id)
                                       ON DELETE CASCADE,

                               CONSTRAINT fk_notifications_job
                                   FOREIGN KEY (job_id)
                                       REFERENCES jobs(id)
                                       ON DELETE CASCADE
);

-- =========================================================
-- INDEXES
-- =========================================================

CREATE INDEX idx_jobs_status
    ON jobs(status);

CREATE INDEX idx_jobs_next_run_at
    ON jobs(next_run_at);

CREATE INDEX idx_jobs_user_id
    ON jobs(user_id);

CREATE INDEX idx_jobs_priority
    ON jobs(priority);

CREATE INDEX idx_execution_logs_job_id
    ON job_execution_logs(job_id);

CREATE INDEX idx_notifications_user_id
    ON notifications(user_id);

-- =========================================================
-- UPDATED_AT AUTO UPDATE FUNCTION
-- =========================================================

CREATE OR REPLACE FUNCTION update_updated_at_column()
RETURNS TRIGGER AS
$$
BEGIN
   NEW.updated_at = NOW();
RETURN NEW;
END;
$$ language 'plpgsql';

-- =========================================================
-- TRIGGER FOR JOBS TABLE
-- =========================================================

CREATE TRIGGER trigger_jobs_updated_at
    BEFORE UPDATE
    ON jobs
    FOR EACH ROW
    EXECUTE FUNCTION update_updated_at_column();

-- =========================================================
-- SAMPLE SCHEDULER QUERY (REFERENCE)
-- =========================================================
--
-- SELECT *
-- FROM jobs
-- WHERE status IN ('SCHEDULED', 'RETRYING')
--   AND next_run_at <= NOW()
-- ORDER BY priority DESC, next_run_at ASC
-- FOR UPDATE SKIP LOCKED
-- LIMIT 10;
--
-- =========================================================