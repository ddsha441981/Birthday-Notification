package com.cwc.birthday.notification.config;

public class ConstantValue {
        public static String[] DEFAULT_BATCH_SQL_TABLE_QUERY = {
                "CREATE TABLE IF NOT EXISTS BATCH_JOB_INSTANCE ( " +
                        "JOB_INSTANCE_ID BIGINT NOT NULL AUTO_INCREMENT, " +
                        "VERSION BIGINT, " +
                        "JOB_NAME VARCHAR(100) NOT NULL, " +
                        "JOB_KEY VARCHAR(32) NOT NULL, " +
                        "PRIMARY KEY (JOB_INSTANCE_ID), " +
                        "UNIQUE KEY JOB_INST_UN (JOB_NAME, JOB_KEY) " +
                        ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;",

                "CREATE TABLE IF NOT EXISTS BATCH_JOB_EXECUTION ( " +
                        "JOB_EXECUTION_ID BIGINT NOT NULL AUTO_INCREMENT, " +
                        "VERSION BIGINT, " +
                        "JOB_INSTANCE_ID BIGINT NOT NULL, " +
                        "CREATE_TIME DATETIME NOT NULL, " +
                        "START_TIME DATETIME DEFAULT NULL, " +
                        "END_TIME DATETIME DEFAULT NULL, " +
                        "STATUS VARCHAR(10), " +
                        "EXIT_CODE VARCHAR(20), " +
                        "EXIT_MESSAGE VARCHAR(2500), " +
                        "LAST_UPDATED DATETIME, " +
                        "PRIMARY KEY (JOB_EXECUTION_ID), " +
                        "FOREIGN KEY (JOB_INSTANCE_ID) REFERENCES BATCH_JOB_INSTANCE(JOB_INSTANCE_ID) " +
                        ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;",

                "CREATE TABLE IF NOT EXISTS BATCH_JOB_EXECUTION_PARAMS ( " +
                        "JOB_EXECUTION_ID BIGINT NOT NULL, " +
                        "PARAMETER_NAME VARCHAR(100) NOT NULL, " +
                        "PARAMETER_TYPE VARCHAR(100) NOT NULL, " +
                        "PARAMETER_VALUE VARCHAR(2500), " +
                        "IDENTIFYING CHAR(1) NOT NULL, " +
                        "PRIMARY KEY (JOB_EXECUTION_ID, PARAMETER_NAME), " +
                        "FOREIGN KEY (JOB_EXECUTION_ID) REFERENCES BATCH_JOB_EXECUTION(JOB_EXECUTION_ID) " +
                        ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;",

                "CREATE TABLE IF NOT EXISTS BATCH_STEP_EXECUTION ( " +
                        "STEP_EXECUTION_ID BIGINT NOT NULL AUTO_INCREMENT, " +
                        "VERSION BIGINT NOT NULL, " +
                        "STEP_NAME VARCHAR(100) NOT NULL, " +
                        "JOB_EXECUTION_ID BIGINT NOT NULL, " +
                        "CREATE_TIME DATETIME NOT NULL, " +
                        "START_TIME DATETIME DEFAULT NULL, " +
                        "END_TIME DATETIME DEFAULT NULL, " +
                        "STATUS VARCHAR(10), " +
                        "COMMIT_COUNT BIGINT, " +
                        "READ_COUNT BIGINT, " +
                        "FILTER_COUNT BIGINT, " +
                        "WRITE_COUNT BIGINT, " +
                        "READ_SKIP_COUNT BIGINT, " +
                        "WRITE_SKIP_COUNT BIGINT, " +
                        "PROCESS_SKIP_COUNT BIGINT, " +
                        "ROLLBACK_COUNT BIGINT, " +
                        "EXIT_CODE VARCHAR(20), " +
                        "EXIT_MESSAGE VARCHAR(2500), " +
                        "LAST_UPDATED DATETIME, " +
                        "PRIMARY KEY (STEP_EXECUTION_ID), " +
                        "FOREIGN KEY (JOB_EXECUTION_ID) REFERENCES BATCH_JOB_EXECUTION(JOB_EXECUTION_ID) " +
                        ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;",

                "CREATE TABLE IF NOT EXISTS BATCH_JOB_EXECUTION_CONTEXT ( " +
                        "JOB_EXECUTION_ID BIGINT NOT NULL, " +
                        "SHORT_CONTEXT VARCHAR(2500) NOT NULL, " +
                        "SERIALIZED_CONTEXT TEXT, " +
                        "PRIMARY KEY (JOB_EXECUTION_ID), " +
                        "FOREIGN KEY (JOB_EXECUTION_ID) REFERENCES BATCH_JOB_EXECUTION(JOB_EXECUTION_ID) " +
                        ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;",

                "CREATE TABLE IF NOT EXISTS BATCH_STEP_EXECUTION_CONTEXT ( " +
                        "STEP_EXECUTION_ID BIGINT NOT NULL, " +
                        "SHORT_CONTEXT VARCHAR(2500) NOT NULL, " +
                        "SERIALIZED_CONTEXT TEXT, " +
                        "PRIMARY KEY (STEP_EXECUTION_ID), " +
                        "FOREIGN KEY (STEP_EXECUTION_ID) REFERENCES BATCH_STEP_EXECUTION(STEP_EXECUTION_ID) " +
                        ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;",

                "CREATE TABLE IF NOT EXISTS BATCH_JOB_SEQ ( " +
                        "ID BIGINT NOT NULL, " +
                        "UNIQUE_KEY CHAR(1) NOT NULL, " +
                        "UNIQUE KEY UNIQUE_KEY_UN (UNIQUE_KEY) " +
                        ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;",

                "INSERT INTO BATCH_JOB_SEQ (ID, UNIQUE_KEY) " +
                        "SELECT 0, '0' WHERE NOT EXISTS (SELECT * FROM BATCH_JOB_SEQ);",

                "CREATE TABLE IF NOT EXISTS BATCH_JOB_EXECUTION_SEQ ( " +
                        "ID BIGINT NOT NULL, " +
                        "UNIQUE_KEY CHAR(1) NOT NULL, " +
                        "UNIQUE KEY UNIQUE_KEY_UN (UNIQUE_KEY) " +
                        ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;",

                "INSERT INTO BATCH_JOB_EXECUTION_SEQ (ID, UNIQUE_KEY) " +
                        "SELECT 0, '0' WHERE NOT EXISTS (SELECT * FROM BATCH_JOB_EXECUTION_SEQ);",

                "CREATE TABLE IF NOT EXISTS BATCH_STEP_EXECUTION_SEQ ( " +
                        "ID BIGINT NOT NULL, " +
                        "UNIQUE_KEY CHAR(1) NOT NULL, " +
                        "UNIQUE KEY UNIQUE_KEY_UN (UNIQUE_KEY) " +
                        ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;",

                "INSERT INTO BATCH_STEP_EXECUTION_SEQ (ID, UNIQUE_KEY) " +
                        "SELECT 0, '0' WHERE NOT EXISTS (SELECT * FROM BATCH_STEP_EXECUTION_SEQ);"
        };
}