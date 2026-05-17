package com.chronos.scheduler;

import org.springframework.scheduling.support.CronExpression;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Objects;

public class CronUtils {
    public static LocalDateTime next(String cron, LocalDateTime from) {

        if (cron == null) {
            throw new IllegalArgumentException("Cron expression required");
        }

        return Objects.requireNonNull(CronExpression.parse(cron)
                        .next(from.atZone(ZoneId.systemDefault())))
                .toLocalDateTime();
    }
}
