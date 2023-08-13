package ru.practicum;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.ewm.dto.stats.StatsDTO;
import ru.practicum.ewm.dto.stats.StatsDtoForSave;

import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
public class StatsClient {
    private  final RestTemplate restTemplate;
    ObjectMapper mapper = new  ObjectMapper();

    public static final String START = "2000-01-01 01:01:01";

    public static final String END = "3000-01-01 01:01:01";

    public StatsClient(RestTemplateBuilder builder) {
        this.restTemplate = builder
                .uriTemplateHandler(new DefaultUriBuilderFactory("http://stats-server:9090"))
                .build();
    }

    public void postHit(String uri, String ip) {
        String app = "ewm-main-service";
        StatsDtoForSave statsDtoForSave = new StatsDtoForSave(app, uri, ip);
        try {
            StatsDtoForSave response = restTemplate.postForObject("/hit", statsDtoForSave, StatsDtoForSave.class);
            log.info("response: " + response);
        } catch (Exception ex) {
            log.error("error", ex);
            throw new RuntimeException("Error in Stats service");
        }


    }

    public Long getViews(String uri) {
        List<String> uris = new ArrayList();
        uris.add(uri);
        Long views = 0L;

        try {
            List<StatsDTO> response = mapper.convertValue(restTemplate.getForObject("/stats?start=" + START + "&end=" + END + "&uris=" + uri + "&unique=true", List.class), new TypeReference<List<StatsDTO>>() {});
            log.info("response = {}", response);
            log.info("1stats = {}", response.get(0));
            views = response.get(0).getHits();
        } catch (Exception ex) {
            log.error("error", ex);
            throw new RuntimeException("Error in Stats service");
        }
        return views;
    }


}
