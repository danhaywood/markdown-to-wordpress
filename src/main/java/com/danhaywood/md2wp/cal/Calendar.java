package com.danhaywood.md2wp.cal;

import java.time.Instant;
import java.time.LocalDate;

import org.springframework.stereotype.Component;

@Component
public class Calendar {

    public String currentMonth() {
        return LocalDate.now().format(java.time.format.DateTimeFormatter.ofPattern("yyyy/MM"));
    }
}
