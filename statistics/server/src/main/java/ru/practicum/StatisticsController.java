package ru.practicum;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.model.StatisticsDto;
import ru.practicum.service.StatisticsService;
import javax.validation.Valid;
import java.util.List;

@Slf4j
@Validated
@RestController
@RequestMapping
@RequiredArgsConstructor
public class StatisticsController {
    private final StatisticsService statisticsService;

    @GetMapping("/stats")
    public List<StatisticsMainDto> getStatisticBy(@RequestParam(value = "start") String start,
                                                  @RequestParam(value = "end") String end,
                                                  @RequestParam(value = "uris", defaultValue = "") List<String> uris,
                                                  @RequestParam(value = "unique", defaultValue = "false") boolean unique) {
        log.info("GET request -> get statistic by parameters");
        return statisticsService.getStatisticsBy(start, end, uris, unique);
    }

    @PostMapping("/hit")
    public StatisticsDto saveStatistic(@RequestBody @Valid StatisticsDto statisticsDto) {
        log.info("POST request -> save new statistic");
        return statisticsService.saveStatistic(statisticsDto);
    }
}