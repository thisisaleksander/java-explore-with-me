package ru.practicum.main.user.service;

import org.springframework.transaction.annotation.Transactional;
import ru.practicum.main.user.User;
import ru.practicum.main.user.dto.UserDto;

import java.util.List;

@Transactional(readOnly = true)
public interface UserService {
    @Transactional
    UserDto saveUser(UserDto userDto);

    @Transactional
    void deleteUser(Long userId);

    List<UserDto> getUsers(List<Long> users, Integer from, Integer size);

    User findById(Long userId);
}
