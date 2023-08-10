package ru.practicum.main.compilation.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.main.compilation.Compilation;

@Repository
public interface CompilationRepository extends JpaRepository<Compilation, Long> {
    Page<Compilation> findByPinned(boolean pinned, Pageable page);
}
