package com.example.springboot;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDate;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ControllerTest {
    @Autowired
    private MockMvc mvc;

    @Test
    void getCurrentDate() throws Exception {
        LocalDate date = LocalDate.now();
        mvc.perform(MockMvcRequestBuilders.get("/currentDate").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(equalTo(date.toString())));
    }

    @Test
    void getCurrentDay() throws Exception {
        LocalDate date = LocalDate.now();
        mvc.perform(MockMvcRequestBuilders.get("/currentDay").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(equalTo(String.valueOf(date.getDayOfMonth()))));
    }

    @Test
    void getCurrentMonth() throws Exception {
        LocalDate date = LocalDate.now();
        mvc.perform(MockMvcRequestBuilders.get("/currentMonth").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(equalTo(String.valueOf(date.getMonthValue()))));
    }

    @Test
    void getCurrentYear() throws Exception {
        LocalDate date = LocalDate.now();
        mvc.perform(MockMvcRequestBuilders.get("/currentYear").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(equalTo(String.valueOf(date.getYear()))));
    }

    @Test
    void postEvent() throws Exception {
        String request = requestParam("postEvent","event1", "2020-01-01");
        mvc.perform(MockMvcRequestBuilders.get(request).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        mvc.perform(MockMvcRequestBuilders.get(request).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest()); // cannot post same event twice
    }

    @Test
    void patchEvent() throws Exception {
        String patchRequest = patchRequestParam("patchEvent","event2", "2020-02-01", "event2", "2020-02-02");
        mvc.perform(MockMvcRequestBuilders.get(patchRequest).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound()); // cannot patch non-existing event

        String oldRequest = requestParam("postEvent","event2", "2020-02-01");
        mvc.perform(MockMvcRequestBuilders.get(oldRequest).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        mvc.perform(MockMvcRequestBuilders.get(patchRequest).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

    }

    @Test
    void deleteEvent() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/postEvent?name=event3&date=2020-01-01").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        mvc.perform(MockMvcRequestBuilders.get("/deleteEvent?name=event3&date=2020-01-01").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void getEventsByDate() throws Exception {
        String postRequest = requestParam("postEvent","event4", "2020-01-01");
        mvc.perform(MockMvcRequestBuilders.get(postRequest).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        mvc.perform(MockMvcRequestBuilders.get("/getEventsByDate?date=2020-01-01").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    public String requestParam(String path, String name, String date) {
        return "/" + path + "?name=" + name + "&date=" + date;
    }

    public String patchRequestParam(String path, String oldName, String oldDate, String newName, String newDate) {
        return "/" + path + "?oldName=" + oldName + "&oldDate=" + oldDate + "&newName=" + newName + "&newDate=" + newDate;
    }
}