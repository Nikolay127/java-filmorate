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

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Collection<User> getUsers() {
        log.info("Вызван метод передачи списка всех пользователей");
        return users.values();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public User createUser(@Valid @RequestBody User user) {
        log.info("Начался процесс создания пользователя {}", user);
        validateUserName(user);
        if (user.getId() != null) {
            log.error("Пользователь {} не создан из-за переданного id", user);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Id нового пользователя генерируется автоматически");
        }
        user.setId(getNextId());
        users.put(user.getId(), user);
        log.info("Процесс обновления пользователя {} - успешно завершен", user);
        return user;

    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public User updateUser(@Valid @RequestBody User user) {
        log.info("Начался процесс обновления пользователя {}", user);
        validateUserName(user);
        //Предполагаем, что нам передали все параметры, в ТЗ не указано иное
        if (!users.containsKey(user.getId())) {
            log.error("Пользователь {} не найден", user);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Пользователь не найден");
        }
        users.put(user.getId(), user);
        log.info("Процесс обновления пользователя {} - успешно завершен", user);
        return user;
    }

    private long getNextId() {
        long currentMaxId = users.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }

    private void validateUserName(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
    }
}