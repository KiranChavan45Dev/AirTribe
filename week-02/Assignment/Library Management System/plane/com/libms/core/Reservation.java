package com.libms.core;

import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;
import java.util.logging.Logger;

import com.libms.notify.NotificationService;

public class Reservation {
    private static final Logger LOG = Logger.getLogger(Reservation.class.getName());

    // isbn -> list of notification services (observers)
    private final Map<String, Queue<NotificationService>> waitingLists = new HashMap<>();

    public void reserve(String isbn, NotificationService observer) {
        waitingLists.computeIfAbsent(isbn, k -> new ArrayDeque<>()).add(observer);
        LOG.info("Reserved ISBN " + isbn + " for " + observer.getPatronId());
    }

    public void notify(String ispn){
        Queue<NotificationService> observers = waitingLists.get(ispn);
        if(observers == null || observers.isEmpty()) {
            LOG.info("No reservations for ISBN " + ispn);
            return;
        }

        NotificationService observer = observers.poll();

        if(observer != null) {
            observer.update(ispn);
            LOG.info("Notified " + observer.getPatronId() + " for ISBN " + ispn);
        } else {
            LOG.warning("No observer found to notify for ISBN " + ispn);
        }
    }

     public boolean hasReservations(String isbn) {
        Queue<NotificationService> queue = waitingLists.get(isbn);
        return queue != null && !queue.isEmpty();
    }
}
