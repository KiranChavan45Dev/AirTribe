package com.libms.notify;

import java.util.logging.Logger;

public class NotificationService {
    private static final Logger logger = Logger.getLogger(NotificationService.class.getName());

    private final String patronId;
    private final String contact; // email

    public NotificationService(String patronId, String contact) {
        this.patronId = patronId;
        this.contact = contact;
    }

    public String getPatronId() { return patronId; }
    public String getContact() { return contact; }

    public void update(String isbn) {
        // In a real system send email. Here we log.
        logger.info(String.format("Notifing %s (%s): Book %s is available", patronId, contact, isbn));
    }
}
