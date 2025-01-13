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
        //Не вижу смысла логировать такие вызовы именно в методе сервиса
        return userStorage.getAllUsers();
    }

    public User createUser(User user) {
        return userStorage.createUser(user);
    }

    public User updateUser(User user) {
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
}
