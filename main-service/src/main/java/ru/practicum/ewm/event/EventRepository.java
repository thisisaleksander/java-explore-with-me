package ru.practicum.ewm.event;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.ewm.category.model.Category;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.user.model.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {

    @Query("select e " +
            "from Event as e " +
            "where e.initiator in ?1 " +
            "and e.state in ?2 " +
            "and e.category in ?3 " +
            "and e.eventDate > ?4 and e.eventDate < ?5 ")
    Page<Event> findByInitiatorAndStateAndCategoryAndEventDateBeforeEnd(List<User> users, List<String> states, List<Category> categories, LocalDateTime start, LocalDateTime end, Pageable page);

    @Query("select e " +
            "from Event as e " +
            "where e.annotation like %?1% " +
            "and e.category.id in (?2) " +
            "and e.paid in (?3) " +
            "and e.eventDate > ?4 and e.eventDate < ?5")
    Page<Event> findByQuery(String text, List<Long> categories, List<Boolean> paid, LocalDateTime rangeStart, LocalDateTime rangeEnd, Pageable page);

    Event findByIdAndInitiator(Long eventId, User initiator);

    Page<Event> findByInitiator(User initiator, Pageable page);

    @Query("select e " +
            "from Event as e " +
            "where e.annotation like %?1% " +
            "and e.paid in (?2) " +
            "and e.eventDate > ?3 and e.eventDate < ?4")
    Page<Event> getEventsNoCategory(String text, List<Boolean> paid, LocalDateTime rangeStart, LocalDateTime rangeEnd, Pageable page);

    @Modifying
    @Query(value = "UPDATE events " +
            "SET confirmed_requests = ?1 " +
            "WHERE id = ?2 ",
            nativeQuery = true)
    void updateConfirmedRequest(int confirmedRequest, Long eventId);

    @Query("select e " +
            "from Event as e " +
            "where e.id in ?1 ")
    List<Event> findByIds(List<Long> events);

    Optional<Event> findByCategory(Category category);
}
