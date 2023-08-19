package ru.practicum.ewm.location.service;

import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.location.model.Location;

@Transactional
public interface LocationService {
    Location save(Location location);
}
