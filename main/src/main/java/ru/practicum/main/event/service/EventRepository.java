package ru.practicum.main.event.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.main.category.Category;
import ru.practicum.main.event.Event;
import ru.practicum.main.user.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {
    @Query("SELECT Event FROM Event WHERE Event.initiator IN ?1 " +
            "AND Event.state IN ?2 AND Event.category IN ?3 " +
            "AND Event.eventDate > ?4 AND Event.eventDate < ?5 ")
    Page<Event> findByInitiatorAndStateAndCategoryAndEventDateBeforeEnd(List<User> users,
                                                                        List<String> states,
                                                                        List<Category> categories,
                                                                        LocalDateTime start,
                                                                        LocalDateTime end,
                                                                        Pageable page);

    @Query("SELECT Event FROM Event WHERE Event.annotation LIKE %?1% " +
            "AND Event.paid IN (?2) AND Event.eventDate > ?3 AND Event.eventDate < ?4")
    Page<Event> getEventsNoCategory(String text,
                                    List<Boolean> paid,
                                    LocalDateTime rangeStart,
                                    LocalDateTime rangeEnd,
                                    Pageable page);

    @Query("SELECT Event from Event WHERE Event.annotation LIKE %?1% " +
            "AND Event.category.id IN (?2) AND Event.paid IN (?3) " +
            "AND Event.eventDate > ?4 AND Event.eventDate < ?5")
    Page<Event> findByQuery(String text,
                            List<Long> categories,
                            List<Boolean> paid,
                            LocalDateTime rangeStart,
                            LocalDateTime rangeEnd,
                            Pageable page);

    Event findByIdAndInitiator(Long eventId, User initiator);

    Page<Event> findByInitiator(User initiator, Pageable page);

    @Modifying
    @Query(value = "UPDATE events SET confirmed_requests = ?1 WHERE id = ?2 ",
            nativeQuery = true)
    void updateConfirmedRequest(int confirmedRequest, Long eventId);

    @Query("SELECT Event FROM Event WHERE Event.id IN ?1 ")
    List<Event> findByIds(List<Long> events);

    Optional<Event> findByCategory(Category category);
}
