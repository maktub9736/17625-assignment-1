package com.example.springboot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;

@RestController
public class Controller {
    Events events = new Events();

    @GetMapping("/currentDate")
    public String getCurrentDate() {
        LocalDate now = LocalDate.now();
        return now.toString();
    }

    @GetMapping("/currentDay")
    public String getCurrentDay() {
        LocalDate now = LocalDate.now();
        return String.valueOf(now.getDayOfMonth());
    }

    @GetMapping("/currentMonth")
    public String getCurrentMonth() {
        LocalDate now = LocalDate.now();
        return String.valueOf(now.getMonthValue());
    }

    @GetMapping("/currentYear")
    public String getCurrentYear() {
        LocalDate now = LocalDate.now();
        return String.valueOf(now.getYear());
    }

    @RequestMapping("/postEvent")
    public String postEvent(@RequestParam String name, @RequestParam String date) {
        boolean success = events.addEvent(name, date);
        if (success) {
            return "Event added successfully";
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Event already exists");
        }
    }

    @RequestMapping("/patchEvent")
    public String patchEvent(@RequestParam String oldName, @RequestParam String oldDate, @RequestParam String newName, @RequestParam String newDate) {
        boolean success = events.deleteEvent(oldName, oldDate);
        if (newName.length() == 0 || newDate.length() == 0) {
            throw new ResponseStatusException(HttpStatus.NO_CONTENT, "Event name or date cannot be empty");
        } else if (!success) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Old event does not exist");
        }

        boolean addNewSuccess = events.addEvent(newName, newDate);
        if (addNewSuccess) {
            return "Event patched successfully";
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "New event already exists");
        }
    }

    @RequestMapping("/deleteEvent")
    public String deleteEvent(@RequestParam String name, @RequestParam String date) {
        boolean success = events.deleteEvent(name, date);
        if (success) {
            return "Event deleted";
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Event does not exist");
        }
    }

    @RequestMapping("/getEventsByDate")
    public String getEventsByDate(@RequestParam String date) {
        return events.getEventsByDate(date).toString();
    }
}
