package com.airtribe.core.invoice.management.handler;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class BackupManager {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");

    public static void backupFile(String sourcePath, String backupDir) {
        try {
            String timestamp = LocalDateTime.now().format(formatter);
            Path source = Paths.get(sourcePath);
            String backupFileName = source.getFileName().toString().replace(".", "_" + timestamp + ".");
            Path target = Paths.get(backupDir, backupFileName);

            Files.createDirectories(Paths.get(backupDir));
            Files.copy(source, target, StandardCopyOption.REPLACE_EXISTING);

            System.out.println("Backup created: " + target.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
