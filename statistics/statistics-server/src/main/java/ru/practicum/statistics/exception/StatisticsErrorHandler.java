package ru.practicum.statistics.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.statistics.exception.model.NotFoundException;
import ru.practicum.statistics.exception.model.ResponseException;
import ru.practicum.statistics.exception.model.ValidationException;

@Slf4j
@RestControllerAdvice
public class StatisticsErrorHandler {
    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public StatisticsErrorResponse handleNotFoundException(final NotFoundException e) {
        log.info("404 {}", e.getMessage());
        return new StatisticsErrorResponse(
                e.getMessage()
        );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public StatisticsErrorResponse handleValidationException(final ValidationException e) {
        log.info("409 {}", e.getMessage());
        return new StatisticsErrorResponse(
                e.getMessage()
        );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public StatisticsErrorResponse handleValidationException(final ResponseException e) {
        log.info("400 {}", e.getMessage());
        return new StatisticsErrorResponse(
                e.getMessage()
        );
    }
}
