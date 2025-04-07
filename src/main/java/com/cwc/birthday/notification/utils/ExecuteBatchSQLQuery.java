package com.cwc.birthday.notification.utils;

import com.cwc.birthday.notification.config.ConstantValue;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;


@Component
@Slf4j
public class ExecuteBatchSQLQuery {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public ExecuteBatchSQLQuery(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @PostConstruct
    public void runSqlScripts() {
        if (!checkIfTableExists("BATCH_JOB_INSTANCE")) {
            executeSqlScript();
        } else {
            log.info("Tables already exist. Skipping creation.");
        }
    }

    private boolean checkIfTableExists(String tableName) {
        String sql = "SELECT COUNT(*) FROM information_schema.tables WHERE table_name = ?";
        Integer count = jdbcTemplate.queryForObject(sql, new Object[]{tableName.toLowerCase()}, Integer.class);
        return count != null && count > 0;
    }

    private void executeSqlScript() {
        String[] sqlStatements = ConstantValue.DEFAULT_BATCH_SQL_TABLE_QUERY;

        for (String sql : sqlStatements) {
            try {
                jdbcTemplate.execute(sql);
                log.info("Executed: " + sql);
            } catch (Exception e) {
                log.error("Error executing SQL: " + sql + " - " + e.getMessage());
            }
        }
    }
}
