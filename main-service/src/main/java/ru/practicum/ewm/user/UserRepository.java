package ru.practicum.ewm.user;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.ewm.user.model.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Query("select MAX(id) " +
            "from User as u ")
    Long findMaxId();

    @Query("select id " +
            "from User as u ")
    List<Long> findAllId();

    Optional<User> findByName(String name);

    @Query("select u " +
            "from User as u " +
            "where u.id in (?1)")
    List<User> findByIds(List<Long> users);

    @Query("select u " +
            "from User as u " +
            "where u.id in ?1")
    Page<User> findByIdsPage(List<Long> users, Pageable page);
}
