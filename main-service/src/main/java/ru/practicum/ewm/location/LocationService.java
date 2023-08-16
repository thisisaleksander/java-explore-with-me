package ru.practicum.ewm.location;

import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface LocationService {
    Location save(Location location);
}
