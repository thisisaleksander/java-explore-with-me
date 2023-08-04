package ru.practicum.service;

import ru.practicum.StatisticsMainDto;
import ru.practicum.model.StatisticsDto;

import java.util.List;

public interface  StatisticsService {

    List<StatisticsMainDto> getStatisticsBy(String start, String end, List<String> uris, boolean unique);

    StatisticsDto saveStatistic(StatisticsDto statisticsDto);
}