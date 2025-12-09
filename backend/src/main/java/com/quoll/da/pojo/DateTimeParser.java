package com.quoll.da.pojo;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateTimeParser {
    public static LocalDateTime parseDateTime(String input) {
        LocalDateTime dateTime = null;


        if (input.length() == 10) {
            // user input is yyyy-MM-dd format，only date
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate localDate = LocalDate.parse(input, dateFormatter);
            dateTime = localDate.atStartOfDay(); // Set the date to the start of the current day
        } else if (input.length() == 19) {
            // user's input is yyyy-MM-ddTHH:mm:ss format，include date and time
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
            dateTime = LocalDateTime.parse(input, dateTimeFormatter);
        } else {
            throw new IllegalArgumentException("Invalid date format");
        }


        return dateTime;
    }
}
