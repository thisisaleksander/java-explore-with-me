package ru.practicum.main.category.service;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.main.category.Category;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    @Query("SELECT Category.id FROM Category")
    List<Long> findAllId();

    Optional<Category> findByName(String name);

    @Query("SELECT Category FROM Category WHERE Category.id IN (?1)")
    List<Category> findByIds(List<Long> categories);
}
