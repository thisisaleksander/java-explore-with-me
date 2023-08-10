package ru.practicum.main.location.service;

import org.springframework.transaction.annotation.Transactional;
import ru.practicum.main.location.Location;

@Transactional
public interface LocationService {
    Location save(Location location);
}
