package com.dyma.tennis.model;

import java.time.LocalDate;

public record Tournament(
        String identifier,
        String name,
        LocalDate startDate,
        LocalDate endDateDate,
        Integer prizeMoney,
        Integer capacity
) {
}
