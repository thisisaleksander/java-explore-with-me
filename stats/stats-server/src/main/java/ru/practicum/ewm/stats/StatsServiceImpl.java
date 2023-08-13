package ru.practicum.ewm.stats;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.dto.stats.StatsDTO;
import ru.practicum.ewm.dto.stats.StatsDtoForSave;
import ru.practicum.ewm.stats.exception.ResponseException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class StatsServiceImpl implements StatsService {

    private final StatsRepository repository;

    static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    public List<StatsDTO> getStatsFromDB(String start, String end, List<String> uris, boolean unique) {
        LocalDateTime from = LocalDateTime.parse(start, dateTimeFormatter);
        LocalDateTime to = LocalDateTime.parse(end, dateTimeFormatter);
        if (from.isAfter(to)) {
            throw new ResponseException("start is after end");
        }
        if (uris == null || uris.size() == 0 || uris.get(0).equals("events/") || uris.get(0).isBlank()) {
            uris = repository.getDistinctUri();
        }
        List<StatsDTO> listDTO = new ArrayList<>();
        List<String> urisToList;
        if (unique) {
            urisToList = repository.getUrisByUriForUniqueIP(uris, from, to);
        } else {
            urisToList = repository.getUrisByUri(uris, from, to);
            }
        for (String uri : uris) {
            StatsDTO statsDTO = new StatsDTO("ewm-main-service", uri, Collections.frequency(urisToList, uri));
            listDTO.add(statsDTO);
        }
        listDTO.sort((dto1, dto2) -> (int) (dto2.getHits() - dto1.getHits()));
        log.info("listDTO = {}", listDTO);
        return listDTO;
    }

    @Override
    public StatsDtoForSave saveStats(StatsDtoForSave statsForSave) {
        Stats stats = new Stats();
        stats.setUri(statsForSave.getUri());
        stats.setApp(statsForSave.getApp());
        stats.setIp(statsForSave.getIp());
        stats.setCreated(LocalDateTime.now());
        repository.save(stats);
        log.info("statsForSave = {}",stats);
        return statsForSave;
    }
}
