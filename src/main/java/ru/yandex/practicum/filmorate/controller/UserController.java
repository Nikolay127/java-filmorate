package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import java.util.Collection;

@RestController
@RequestMapping("/users")
public class UserController {

    private static final Logger log = LoggerFactory.getLogger(UserController.class);
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Collection<User> getUsers() {
        log.info("В контроллере {} запущен метод получения всех пользователей", UserController.class.getName());
        return userService.getAllUsers();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public User createUser(@Valid @RequestBody User user) {
        log.info("В контроллере {} запущен метод для создания пользователя", UserController.class.getName());
        return userService.createUser(user);

    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public User updateUser(@Valid @RequestBody User user) {
        log.info("В контроллере {} запущен метод для обновления пользователя", UserController.class.getName());
        return userService.updateUser(user);
    }

    @PutMapping("/{id}/friends/{friendId}")
    @ResponseStatus(HttpStatus.OK)
    public User addToFriends(@PathVariable Long id, @PathVariable Long friendId) {
        log.info("В контроллере {} запущен метод для обоюдного добавления в друзьям", UserController.class.getName());
        return userService.addToFriends(id, friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    @ResponseStatus(HttpStatus.OK)
    public User removeFromFriends(@PathVariable Long id, @PathVariable Long friendId) {
        log.info("В контроллере {} запущен метод для обоюдного удаления из друзей", UserController.class.getName());
        return userService.removeFromFriends(id, friendId);
    }

    @GetMapping("/{id}/friends")
    @ResponseStatus(HttpStatus.OK)
    public Collection<User> getAllFriends(@PathVariable Long id) {
        log.info("В контроллере {} запущен метод для получения списка всех друзей у пользователя",
                UserController.class.getName());
        return userService.getAllFriends(id);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    @ResponseStatus(HttpStatus.OK)
    public Collection<User> getAllCommonFriends(@PathVariable Long id, @PathVariable Long otherId) {
        log.info("В контроллере {} запущен метод для получения списка общих друзей у двух пользователей",
                UserController.class.getName());
        return userService.getAllCommonFriends(id, otherId);
    }
}
