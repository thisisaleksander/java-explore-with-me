package ru.practicum.ewm.user.model;

import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
public class UserMapper {

        public static User toEntityFromDto(UserDto userDto) {
            User user = new User();
            user.setName(userDto.getName());
            user.setId(userDto.getId());
            user.setEmail(userDto.getEmail());
            return user;
        }

        public static UserDto toDtoFromEntity(User user) {
            UserDto userDto = new UserDto();
            userDto.setName(user.getName());
            userDto.setId(user.getId());
            userDto.setEmail(user.getEmail());
            return userDto;
        }

        public static List<UserDto> toListDtoFromListEntity(List<User> listEntity) {
            List<UserDto> listDto = new ArrayList<>();
            for (User user:listEntity) {
                listDto.add(toDtoFromEntity(user));
            }
            return listDto;
        }
}
