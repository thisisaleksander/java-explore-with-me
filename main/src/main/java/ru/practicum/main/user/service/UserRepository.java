package ru.practicum.main.user.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.main.user.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    @Query("SELECT u FROM User as u where u.id in ?1")
    List<User> findByIds(List<Long> users);

    @Query("SELECT u FROM User as u where u.id in ?1")
    Page<User> findByIdsPage(List<Long> users, Pageable page);

    @Query("SELECT u.id FROM User as u")
    List<Long> findAllId();

    Optional<User> findByName(String name);
}
