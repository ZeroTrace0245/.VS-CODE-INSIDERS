package com.zerotrace.smartfacility.service;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class ReminderScheduler {

    private static final Logger log = LoggerFactory.getLogger(ReminderScheduler.class);

    // Placeholder scheduled job for overdue reminders / upcoming bookings
    @Scheduled(cron = "0 0 7 * * *")
    public void sendDailyReminders() {
        log.info("Running daily reminder job (hook up to mail/SMS)");
    }
}
