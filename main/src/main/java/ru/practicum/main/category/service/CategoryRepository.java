package ru.practicum.main.category.service;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.main.category.Category;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    @Query(value = "SELECT c.id FROM Category as c")
    List<Long> findAllId();

    Optional<Category> findByName(String name);

    @Query("SELECT c FROM Category as c WHERE c.id IN (?1)")
    List<Category> findByIds(List<Long> categories);
}
