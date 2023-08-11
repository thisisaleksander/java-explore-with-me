package ru.practicum.statistics.exception;

public class StatisticsErrorResponse {
    private final String error;

    public StatisticsErrorResponse(String error) {
        this.error = error;
    }

    public String getError() {
        return error;
    }
}
