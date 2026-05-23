package com.watad.services.backup;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
@Service
@Slf4j
@RequiredArgsConstructor
public class BackupService {


    @Value("${database.name}")
    private String dbName;

    @Value("${database.user}")
    private String dbUser;

    @Value("${database.password}")
    private String dbPassword;

    @Value("${backup.path}")
    private String backupPath;

    @Value("${backup.every.cron}")
    private String cron;

    @Value("${mysql.dump.path}")
    private String mysqldumpPath;

    @Value("${email.to}")
    private  String ADMIN_EMAIL;

    private final EmailService emailService;

    @Scheduled(cron = "${backup.every.cron}")
    public void jobForBackUp() {

        try {

            File dir = new File(backupPath);
            if (!dir.exists()) {
                dir.mkdirs();
            }

            String timestamp = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss")
                    .format(new Date());

            String backupFile = backupPath + dbName + "_" + timestamp + ".sql";

            ProcessBuilder pb = new ProcessBuilder(
                    mysqldumpPath,
                    "-h", "63.250.52.123",
                    "-u", dbUser,
                    "-p" + dbPassword,
                    dbName
            );

            // write output manually to file
            pb.redirectErrorStream(true);
            pb.redirectOutput(new File(backupFile));

            Process process = pb.start();

            int result = process.waitFor();

            if (result == 0) {

                log.info("Backup created successfully: {}", backupFile);

                File file = new File(backupFile);

                emailService.sendBackupEmail(ADMIN_EMAIL, file);

                log.info("Backup emailed successfully");

                if (file.exists()) {
                    boolean deleted = file.delete();
                    log.info("Backup file deleted: {}", deleted);
                }
            } else {
                log.error("Backup failed with exit code: {}", result);
            }

        } catch (Exception e) {
            log.error("Backup error", e);
        }
    }
    public void callJob(){
        jobForBackUp();
        log.info("calling service done ");
    }
}
