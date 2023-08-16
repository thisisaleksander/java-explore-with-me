package ru.practicum.ewm.request;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.request.model.Request;
import ru.practicum.ewm.user.model.User;

import java.util.List;

@Transactional
@Repository
public interface RequestRepository extends JpaRepository<Request, Long> {

    List<Request> findByRequesterId(Long userId);

    @Modifying
    @Query(value = "UPDATE requests " +
            "SET status = ?1 " +
            "WHERE id in (?2) ",
            nativeQuery = true)
    void confirmRequestStatus(String status, List<Long> requestIds);

    List<Request> findByRequester(User requester);

    List<Request> findByEventId(Long eventId);

    List<Request> findAllByStatusAndEventId(String confirmed, Long eventId);
}
