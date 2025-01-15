package ru.yandex.practicum.filmorate.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Service
public class UserService {

    private static final Logger log = LoggerFactory.getLogger(UserService.class);
    private final UserStorage userStorage;

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public Collection<User> getAllUsers() {
        log.info("В сервисе {} запущен метод по получению списка всех пользователей", UserService.class.getName());
        return userStorage.getAllUsers();
    }

    public User createUser(User user) {
        log.info("В сервисе {} запущен метод по созданию пользователя", UserService.class.getName());
        validateBlankFields(user);
        log.info("У пользователя {} успешно пройдена валидация пустых полей", user);
        checkUserName(user);
        log.info("У пользователя {} успешно пройдена валидация имени", user);
        log.info("Вызывается метод по созданию пользователя {}", user);
        return userStorage.createUser(user);
    }

    public User updateUser(User user) {
        log.info("В сервисе {} запущен метод по обновлению пользователя", UserService.class.getName());
        validateBlankFields(user);
        log.info("У обновляемого пользователя {} успешно пройдена валидация пустых полей", user);
        checkUserName(user);
        log.info("У обновляемого пользователя {} успешно пройдена валидация имени", user);
        log.info("Вызывается метод у {} по обновлению пользователя {}", UserStorage.class.getName(), user);
        return userStorage.updateUser(user);
    }

    public Collection<User> getAllFriends(Long id) {
        log.info("Вызван метод получения списка всех друзей у пользователя с id {}", id);
        if (!isUserExist(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Пользователя не существует");
        }
        User user = userStorage.getUser(id);
        Set<User> allFriends = user.getFriends();
        log.info("Сформирован и передан список всех друзей у пользователя {}: {}", user, allFriends);
        return allFriends;
    }

    public Collection<User> getAllCommonFriends(Long id, Long otherId) {
        log.info("Вызван метод получения списка общих друзей у пользователя с id {} с пользователем с id {}", id, otherId);
        User user = userStorage.getUser(id);
        User otherUser = userStorage.getUser(otherId);
        if (user == null || otherUser == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Пользователь не найден");
        }
        // Получаем общих друзей, пересекаем два множества
        Set<User> commonFriends = new HashSet<>(user.getFriends());
        commonFriends.retainAll(otherUser.getFriends());  // Пересечение коллекций
        return commonFriends;
    }

    public User addToFriends(Long id, Long friendId) {
        log.info("Вызван метод добавления пользователя с id {} в друзья пользователю с id {}", friendId, id);
        if (!isUserExist(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Пользователя не существует");
        }
        if (!isUserExist(friendId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Пользователя не существует");
        }
        final User user = userStorage.getUser(id);
        final User friend = userStorage.getUser(friendId);
        user.getFriends().add(friend);
        log.info("Пользователь {} добавлен в друзья у пользователя {}", friend, user);
        friend.getFriends().add(user);
        log.info("Пользователь {} добавлен в друзья у пользователя {}", user, friend);
        return user;
    }


    public User removeFromFriends(Long id, Long friendId) {
        log.info("Вызван метод удаления пользователя с id {} из друзей у пользователя с id {}", friendId, id);
        if (!isUserExist(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Пользователя не существует");
        }
        if (!isUserExist(friendId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Пользователя не существует");
        }
        User user = userStorage.getUser(id);
        User friend = userStorage.getUser(friendId);
        user.getFriends().remove(friend);
        log.info("Пользователь {} удалён из друзей у пользователя {}", friend, user);
        friend.getFriends().remove(user);
        log.info("Пользователь {} удалён из друзей у пользователя {}", friend, user);
        return user;
    }

    private boolean isUserExist(Long userId) {
        return userStorage.getAllUsers().contains(userStorage.getUser(userId));
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
}
