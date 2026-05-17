package com.chronos.scheduler;

import com.chronos.exception.InvalidCronExpressionException;
import org.springframework.scheduling.support.CronExpression;

import java.time.LocalDateTime;

public class CronUtils {

    public static CronExpression parseExpression(String cron) {
        try {
            return CronExpression.parse(cron);
        } catch (Exception e) {
            throw new InvalidCronExpressionException("Invalid cron expression: " + cron);
        }
    }

    public static LocalDateTime next(String cron, LocalDateTime from) {
        CronExpression expression = parseExpression(cron);

        LocalDateTime next = expression.next(from);

        if (next == null) {
            throw new IllegalStateException("Unable to compute next cron execution");
        }

        return next;
    }
}