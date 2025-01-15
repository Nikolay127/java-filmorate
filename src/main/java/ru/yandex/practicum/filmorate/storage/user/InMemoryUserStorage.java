package ru.yandex.practicum.filmorate.storage.user;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Component
public class InMemoryUserStorage implements UserStorage {

    private static final Logger log = LoggerFactory.getLogger(InMemoryUserStorage.class);
    private final Map<Long, User> users = new HashMap<>();
    private Long userIdCounter = 0L;

    @Override
    public Collection<User> getAllUsers() {
        log.info("Вызван метод передачи списка всех пользователей");
        Collection<User> allUsers = users.values();
        log.info("Сформирован и передан список всех пользователей: {}", allUsers);
        return allUsers;
    }

    //метод для быстрого поиска одного конкретного пользователя
    public User getUser(Long id) {
        log.info("В хранилище {} вызван метод по получению пользователя", InMemoryUserStorage.class.getName());
        return users.get(id);
    }

    @Override
    public User createUser(User user) {
        log.info("Начался процесс создания пользователя {}", user);
        if (user.getId() != null) {
            log.error("Пользователь {} не создан из-за переданного id", user);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Id нового пользователя генерируется автоматически");
        }
        setUserId(user);
        users.put(user.getId(), user);
        log.info("Процесс создания пользователя {} - успешно завершен", user);
        return user;
    }

    @Override
    public User updateUser(User user) {
        log.info("Начался процесс обновления пользователя {}", user);
        if (!users.containsKey(user.getId())) {
            log.error("Пользователь {} не найден", user);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Пользователь не найден");
        }
        users.put(user.getId(), user);
        log.info("Процесс обновления пользователя {} - успешно завершен", user);
        return user;
    }

    @Override
    public User deleteUser(User user) {
        log.info("Начался процесс удаления пользователя {}", user);
        if (!users.containsKey(user.getId())) {
            log.error("Пользователь {} не найден", user);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Пользователь не найден");
        }
        users.remove(user.getId());
        log.info("Процесс удаления пользователя {} - успешно завершен", user);
        return user;
    }

    private void setUserId(User user) {
        user.setId(++userIdCounter);
    }
}
