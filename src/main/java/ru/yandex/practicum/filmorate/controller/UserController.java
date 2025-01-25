package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.RequestUser;
import ru.yandex.practicum.filmorate.model.dto.UserDto;
import ru.yandex.practicum.filmorate.service.UserService;

import java.util.List;
import java.util.Optional;

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
    public Optional<List<UserDto>> getUsers() {
        log.debug("В контроллере {} запущен метод получения всех пользователей", UserController.class.getName());
        return userService.getAllUsers();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Optional<UserDto>> createUser(@Valid @RequestBody RequestUser user) {
        log.debug("В контроллере {} запущен метод для создания пользователя", UserController.class.getName());
        return ResponseEntity.ok(userService.createUser(user));

    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Optional<UserDto>> updateUser(@Valid @RequestBody RequestUser user) {
        log.debug("В контроллере {} запущен метод для обновления пользователя", UserController.class.getName());
        return ResponseEntity.ok(userService.updateUser(user));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteUser(@PathVariable int id) {
        log.debug("В контроллере {} запущен метод для удаления пользователя", UserController.class.getName());
        userService.deleteUser(id);
    }

    @PutMapping("/{id}/friends/{friendId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<UserDto> addToFriends(@PathVariable int id, @PathVariable int friendId) {
        log.debug("В контроллере {} запущен метод для обоюдного добавления в друзьям", UserController.class.getName());
        Optional<UserDto> newFriend = userService.addFriend(friendId, id);
        return newFriend.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.badRequest().build());
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    @ResponseStatus(HttpStatus.OK)
    public void removeFromFriends(@PathVariable int id, @PathVariable int friendId) {
        log.debug("В контроллере {} запущен метод для обоюдного удаления из друзей", UserController.class.getName());
        userService.deleteFriend(friendId, id);
    }

    @GetMapping("/{id}/friends")
    @ResponseStatus(HttpStatus.OK)
    public Optional<List<UserDto>> getAllFriends(@PathVariable int id) {
        log.debug("В контроллере {} запущен метод для получения списка всех друзей у пользователя",
                UserController.class.getName());
        return userService.getFriendsUser(id);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Optional<List<UserDto>>> getAllCommonFriends(@PathVariable int id, @PathVariable int otherId) {
        log.debug("В контроллере {} запущен метод для получения списка общих друзей у двух пользователей",
                UserController.class.getName());
        return ResponseEntity.ok(userService.getCommonFriends(id, otherId));
    }
}
