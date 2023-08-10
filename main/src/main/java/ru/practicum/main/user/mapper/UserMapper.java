package ru.practicum.main.user.mapper;

import lombok.NoArgsConstructor;
import ru.practicum.main.user.User;
import ru.practicum.main.user.dto.UserDto;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
public class UserMapper {
        public static User mapToUser(UserDto userDto) {
            User user = new User();
            user.setName(userDto.getName());
            user.setId(userDto.getId());
            user.setEmail(userDto.getEmail());
            return user;
        }

        public static UserDto mapToUserDto(User user) {
            UserDto userDto = new UserDto();
            userDto.setName(user.getName());
            userDto.setId(user.getId());
            userDto.setEmail(user.getEmail());
            return userDto;
        }

        public static List<UserDto> mapToMultipleUsersDto(List<User> listEntity) {
            List<UserDto> listDto = new ArrayList<>();
            for (User user:listEntity) {
                listDto.add(mapToUserDto(user));
            }
            return listDto;
        }
}
