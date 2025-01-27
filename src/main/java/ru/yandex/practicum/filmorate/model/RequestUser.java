package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.dto.UserDto;

import java.time.LocalDate;
import java.util.Objects;

@Slf4j
@Component
@Data
@RequiredArgsConstructor
public class RequestUser {
    Integer id;
    String email;
    String login;
    String name;
    LocalDate birthday;

    public UserDto getUserDto() {
        log.info("В классе {} вызван метод по получению представления UserDto", RequestUser.class.getName());
        UserDto user = new UserDto();
        if (Objects.nonNull(id)) {
            user.setId(id);
        }
        user.setEmail(email);
        user.setLogin(login);
        user.setName(name);
        user.setBirthday(birthday);
        return user;
    }
}
