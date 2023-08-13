package ru.practicum.ewm.stats;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.dto.stats.StatsDTO;
import ru.practicum.ewm.dto.stats.StatsDtoForSave;

import javax.validation.Valid;
import java.util.List;


@RestController
@RequestMapping
@RequiredArgsConstructor
@Slf4j
@Validated
public class StatsController {
    private final StatsService statsService;

    @GetMapping("/stats")
    public List<StatsDTO> getStats(@RequestParam(value = "start") String start,
                                   @RequestParam(value = "end") String end,
                                   @RequestParam(value = "uris", defaultValue = "") List<String> uris,
                                   @RequestParam(value = "unique", defaultValue = "false") boolean unique) {
        log.info("Get stats start{}, end{}, uris{}, unique={}", start, end, uris, unique);
        return statsService.getStatsFromDB(start, end, uris, unique);
    }

    @PostMapping("/hit")
    public ResponseEntity<StatsDtoForSave> saveStats(@RequestBody @Valid StatsDtoForSave statsForSave) {
        log.info("Post hit for {}", statsForSave);
        return new ResponseEntity<StatsDtoForSave>(statsService.saveStats(statsForSave), HttpStatus.CREATED);
    }
}
