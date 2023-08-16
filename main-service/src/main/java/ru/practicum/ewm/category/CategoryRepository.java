package ru.practicum.ewm.category;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.ewm.category.model.Category;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    @Query("select MAX(id) " +
            "from Category as c ")
    Integer findMaxId();

    @Query("select id " +
            "from Category as c ")
    List<Long> findAllId();

    Optional<Category> findByName(String name);

    @Query("select c " +
            "from Category as c " +
            "where c.id in (?1)")
    List<Category> findByIds(List<Long> categories);
}
