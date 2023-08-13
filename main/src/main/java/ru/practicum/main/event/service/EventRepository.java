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
    @Query(value = "SELECT e FROM Event as e WHERE e.initiator IN ?1 " +
            "AND e.state IN ?2 AND e.category IN ?3 " +
            "AND e.eventDate > ?4 AND e.eventDate < ?5 ")
    Page<Event> findByInitiatorAndStateAndCategoryAndEventDateBeforeEnd(List<User> users,
                                                                        List<String> states,
                                                                        List<Category> categories,
                                                                        LocalDateTime start,
                                                                        LocalDateTime end,
                                                                        Pageable page);

    @Query(value = "SELECT e FROM Event as e WHERE e.annotation LIKE %?1% " +
            "AND e.paid IN ?2 AND e.eventDate > ?3 AND e.eventDate < ?4")
    Page<Event> getEventIgnoreCategory(String text,
                                       List<Boolean> paid,
                                       LocalDateTime start,
                                       LocalDateTime end,
                                       Pageable page);

    @Query(value = "SELECT e FROM Event as e WHERE e.annotation LIKE %?1% " +
            "AND e.category.id IN ?2 AND e.paid IN ?3 " +
            "AND e.eventDate > ?4 AND e.eventDate < ?5")
    Page<Event> findBy(String text,
                       List<Long> categories,
                       List<Boolean> paid,
                       LocalDateTime start,
                       LocalDateTime end,
                       Pageable page);

    Event findByIdAndInitiator(Long eventId, User initiator);

    Page<Event> findByInitiator(User initiator, Pageable page);

    @Modifying
    @Query(value = "UPDATE events SET confirmed_requests = ?1 WHERE id = ?2 ",
            nativeQuery = true)
    void updateConfirmedRequest(int confirmedRequest, Long eventId);

    @Query(value = "SELECT e FROM Event as e WHERE e.id IN ?1 ")
    List<Event> findById(List<Long> eventId);

    Optional<Event> findByCategory(Category category);
}
