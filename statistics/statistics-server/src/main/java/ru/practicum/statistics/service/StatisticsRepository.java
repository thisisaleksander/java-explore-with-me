package ru.practicum.statistics.service;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

import ru.practicum.statistics.model.Statistics;

@Repository
public interface StatisticsRepository extends JpaRepository<Statistics, Long> {

    @Query("SELECT DISTINCT s.uri FROM Statistics as s")
    List<String> getDistinctUri();

    @Query(value = "SELECT uri FROM " +
            "(SELECT DISTINCT ON (ip) uri FROM Statistics " +
            "WHERE uri IN ?1 AND created > ?2 AND created < ?3) AS t",
            nativeQuery = true)
    List<String> getUrisByUriForUniqueIP(List<String> uris, LocalDateTime start, LocalDateTime end);

    @Query("SELECT s.uri FROM Statistics as s WHERE s.uri in ?1 " +
            "AND s.created > ?2 AND s.created < ?3")
    List<String> getUrisByUri(List<String> uris, LocalDateTime dateTimeFrom, LocalDateTime dateTimeTo);
}