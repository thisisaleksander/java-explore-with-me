package ru.practicum.ewm.stats.service;

import ru.practicum.ewm.dto.stats.StatsDTO;
import ru.practicum.ewm.dto.stats.StatsDtoForSave;

import java.util.List;

public interface StatsService {

    List<StatsDTO> getStatsFromDB(String start, String end, List<String> uris, boolean unique);

    StatsDtoForSave saveStats(StatsDtoForSave statsForSave);
}
