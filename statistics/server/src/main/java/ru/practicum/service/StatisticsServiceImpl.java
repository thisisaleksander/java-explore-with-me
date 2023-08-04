package ru.practicum.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.StatisticsMainDto;
import ru.practicum.model.Statistics;
import ru.practicum.model.StatisticsDto;
import ru.practicum.utils.DateUtils;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static ru.practicum.utils.DateUtils.dateTimeFormatter;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class StatisticsServiceImpl implements StatisticsService {

    StatisticsRepository statisticsRepository;
    DateUtils utils;

    @Override
    public List<StatisticsMainDto> getStatisticsBy(String start, String end, List<String> uris, boolean unique) {
        LocalDateTime dateTimeFrom = LocalDateTime.parse(start, dateTimeFormatter);
        LocalDateTime dateTimeTo = LocalDateTime.parse(end, dateTimeFormatter);

        if (uris == null || uris.size() == 0 || uris.get(0).equals("events/") || uris.get(0).isBlank()) {
            uris = statisticsRepository.getDistinctUri();
        }

        List<StatisticsMainDto> mainDto = new ArrayList<>();
        List<String> urisToList;

        if (unique) {
            urisToList = statisticsRepository.getUrisByUriForUniqueIP(uris, dateTimeFrom, dateTimeTo);
        } else {
            urisToList = statisticsRepository.getUrisByUri(uris, dateTimeFrom, dateTimeTo);
        }

        for (String uri : uris) {
            StatisticsMainDto statistic = new StatisticsMainDto("ewm-main-service", uri, Collections.frequency(urisToList, uri));
            mainDto.add(statistic);
        }

        mainDto.sort((dto1, dto2) -> (dto2.getHits() - dto1.getHits()));

        return mainDto;
    }

    @Override
    public StatisticsDto saveStatistic(StatisticsDto statisticsDto) {
        Statistics stats = new Statistics();

        stats.setUri(statisticsDto.getUri());
        stats.setApp(statisticsDto.getApp());
        stats.setIp(statisticsDto.getIp());
        stats.setCreated(utils.now());

        statisticsRepository.save(stats);
        return statisticsDto;
    }
}