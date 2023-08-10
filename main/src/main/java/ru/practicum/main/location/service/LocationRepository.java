package ru.practicum.main.location.service;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.main.location.Location;

@Repository
public interface LocationRepository extends JpaRepository<Location, Long> { }
