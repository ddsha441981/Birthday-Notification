package com.cwc.birthday.notification.utils.schedulers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class BatchScheduler {
    private static final Logger log = LoggerFactory.getLogger(BatchScheduler.class);

    private JobLauncher jobLauncher;
    private Job importBirthdayJob;

    public BatchScheduler(JobLauncher jobLauncher, Job importBirthdayJob) {
        this.jobLauncher = jobLauncher;
        this.importBirthdayJob = importBirthdayJob;
    }

//    @Scheduled(cron = "0 0 0 * * *") // Runs every day at midnight
    @Scheduled(cron = "0 * * * * *")
    public void runBatchJob() {
        try {
            JobParameters jobParameters = new JobParametersBuilder()
                    .addLong("startTime", System.currentTimeMillis())
                    .toJobParameters();
            jobLauncher.run(importBirthdayJob, jobParameters);
            log.info("Birthday batch job started successfully at midnight");
        } catch (Exception e) {
            log.error("Failed to start birthday batch job: {}", e.getMessage(), e);
        }
    }
}
