package com.example.springboot;

import java.util.HashMap;
import java.util.HashSet;

public class Events {
    HashMap<String, HashSet<String>> scheduler;

    public Events() {
        scheduler = new HashMap<>();
    }

    public boolean addEvent(String name, String date) {
        if (scheduler.containsKey(date) && scheduler.get(date).contains(name)) {
            return false;
        }

        if (scheduler.containsKey(date)) {
            scheduler.get(date).add(name);
        } else {
            HashSet<String> events = new HashSet<>();
            events.add(name);
            scheduler.put(date, events);
        }
        return true;
    }

    public HashSet<String> getEventsByDate(String date) {
        if (scheduler.containsKey(date)) {
            return scheduler.get(date);
        } else {
            return new HashSet<>();
        }
    }

    public boolean hasEvent(String name, String date) {
        if (scheduler.containsKey(date)) {
            return scheduler.get(date).contains(name);
        }
        return false;
    }

    public boolean deleteEvent(String name, String date) {
        if (scheduler == null ||date.length() == 0 || name.length() == 0) {
            return false;
        }
        if (!scheduler.containsKey(date) || !scheduler.get(date).contains(name)) {
            return false;
        }
        scheduler.get(date).remove(name);
        return true;
    }
}
