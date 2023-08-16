package ru.practicum.ewm.user.service;

import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.user.model.User;
import ru.practicum.ewm.user.dto.UserDto;

import java.util.List;

@Transactional(readOnly = true)
public interface UserService {
    List<UserDto> getUsers(List<Long> users, Integer from, Integer size);

    @Transactional
    UserDto saveUser(UserDto userDto);

    @Transactional
    void deleteUser(Long userId);

    User findById(Long userId);
}
