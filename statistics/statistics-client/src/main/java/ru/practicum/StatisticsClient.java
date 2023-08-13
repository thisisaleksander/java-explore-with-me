package ru.practicum;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;

import java.util.ArrayList;
import java.util.List;

import static ru.practicum.constants.Constants.STATISTICS_CLIENT_LINK;
import static ru.practicum.utils.DateUtils.END;
import static ru.practicum.utils.DateUtils.START;

@Slf4j
@Component
public class StatisticsClient {
    private final RestTemplate restTemplate;
    ObjectMapper mapper = new  ObjectMapper();

    public StatisticsClient(RestTemplateBuilder builder) {
        this.restTemplate = builder
                .uriTemplateHandler(new DefaultUriBuilderFactory(STATISTICS_CLIENT_LINK))
                .build();
    }

    public void postHit(String uri, String ip) {
        String app = "ewm-main-service";
        StatisticsLocalDto statsDtoForSave = new StatisticsLocalDto(app, uri, ip);
        try {
            StatisticsLocalDto response = restTemplate.postForObject("/hit", statsDtoForSave, StatisticsLocalDto.class);
            log.info("response: " + response);
        } catch (Exception ex) {
            log.error("error", ex);
            throw new RuntimeException("Error in Stats service");
        }
    }

    public Long getViews(String uri) {
        List<String> uris = new ArrayList<>();
        uris.add(uri);
        Long views = 0L;
        try {
            List<StatisticsDto> response = mapper.convertValue(
                    restTemplate.getForObject("/stats?start=" + START + "&end=" + END + "&uris=" + uri + "&unique=true", List.class),
                    new TypeReference<List<StatisticsDto>>() {});
            log.info("response = {}", response);
            log.info("1stats = {}", response.get(0));
            views = Long.valueOf(response.get(0).getHits());
        } catch (Exception ex) {
            log.error("error", ex);
            throw new RuntimeException("Error in Stats service");
        }
        return views;
    }


}
