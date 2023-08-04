package ru.practicum.service;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;
import ru.practicum.model.Statistics;

@Repository
public interface StatisticsRepository extends JpaRepository<Statistics, Long> {

    @Query("SELECT DISTINCT uri FROM Statistics")
    List<String> getDistinctUri();

    @Query(value = "SELECT t.uri FROM (SELECT DISTINCT ON (ip) uri FROM Statistics " +
            "WHERE uri IN (?1) AND created > ?2 AND created < ?3) AS t",
            nativeQuery = true)
    List<String> getUrisByUriForUniqueIP(List<String> uris, LocalDateTime dateTimeFrom, LocalDateTime dateTimeTo);

    @Query("SELECT uri FROM Statistics WHERE uri in (?1) " +
            "AND created > ?2 AND created < ?3")
    List<String> getUrisByUri(List<String> uris, LocalDateTime dateTimeFrom, LocalDateTime dateTimeTo);
}