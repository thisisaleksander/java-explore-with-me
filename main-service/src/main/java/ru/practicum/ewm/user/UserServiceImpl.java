package ru.practicum.ewm.user;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.exception.NotFoundException;
import ru.practicum.ewm.exception.ValidationException;
import ru.practicum.ewm.user.model.User;
import ru.practicum.ewm.user.model.UserDto;
import ru.practicum.ewm.user.model.UserMapper;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class UserServiceImpl implements UserService {

    UserRepository userRepository;

    @Override
    public List<UserDto> getUsers(List<Long> users, Integer from, Integer size) {
        Sort sortByDate = Sort.by(Sort.Direction.ASC, "id");
        int pageIndex = from / size;
        Pageable page = PageRequest.of(pageIndex, size, sortByDate);
        if (users.isEmpty()) {
            users = userRepository.findAllId();
        }
        return UserMapper.toListDtoFromListEntity(userRepository.findByIdsPage(users, page).toList());
    }

    @Override
    public UserDto saveUser(UserDto userDto) {
        if (userRepository.findByName(userDto.getName()).isPresent()) {
            throw new ValidationException("user exist");
        }
        return UserMapper.toDtoFromEntity(userRepository.save(UserMapper.toEntityFromDto(userDto)));
    }

    @Override
    public void deleteUser(Long userId) {
        userRepository.deleteById(userId);
    }

    @Override
    public User findById(Long userId) {
        return userRepository.findById(userId).orElseThrow(() -> new NotFoundException("User not found"));
    }

}
