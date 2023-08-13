package ru.practicum.statistics.service;

import ru.practicum.StatisticsDto;
import ru.practicum.StatisticsLocalDto;

import java.util.List;

public interface  StatisticsService {
    List<StatisticsDto> getStatisticsBy(String start, String end, List<String> uris, boolean unique);

    StatisticsLocalDto saveStatistic(StatisticsLocalDto statisticsLocalDto);
}