package ru.practicum.ewm.location;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class LocationServiceImpl implements LocationService {

    LocationRepository locationRepository;

    @Override
    public Location save(Location location) {
        return locationRepository.save(location);
    }



}
