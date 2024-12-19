package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/users")
public class UserController {

    private static final Logger log = LoggerFactory.getLogger(UserController.class);
    private final Map<Long, User> users = new HashMap<>();
    private Long userIdCounter = 0L;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Collection<User> getUsers() {
        log.info("Вызван метод передачи списка всех пользователей");
        Collection<User> allUsers = users.values();
        log.info("Сформирован и передан список всех пользователей: {}", allUsers);
        return allUsers;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public User createUser(@Valid @RequestBody User user) {
        log.info("Начался процесс создания пользователя {}", user);
        validateBlankFields(user);
        checkUserName(user);
        if (user.getId() != null) {
            log.error("Пользователь {} не создан из-за переданного id", user);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Id нового пользователя генерируется автоматически");
        }
        setUserId(user);
        users.put(user.getId(), user);
        log.info("Процесс обновления пользователя {} - успешно завершен", user);
        return user;

    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public User updateUser(@Valid @RequestBody User user) {
        log.info("Начался процесс обновления пользователя {}", user);
        validateBlankFields(user);
        checkUserName(user);
        //Предполагаем, что нам передали все параметры, в ТЗ не указано иное
        if (!users.containsKey(user.getId())) {
            log.error("Пользователь {} не найден", user);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Пользователь не найден");
        }
        users.put(user.getId(), user);
        log.info("Процесс обновления пользователя {} - успешно завершен", user);
        return user;
    }

    private void checkUserName(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
    }

    private void validateBlankFields(User user) {
        if (user.getEmail().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email" +
                    " нового пользователя не может быть пустым");
        } else if (user.getLogin().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Логин" +
                    " нового пользователя не может быть пустым");
        }
    }

    private void setUserId(User user) {
        user.setId(++userIdCounter);
    }
}
