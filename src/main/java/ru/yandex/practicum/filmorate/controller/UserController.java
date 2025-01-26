package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
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

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Optional<List<UserDto>> getUsers() {
        return userService.getAllUsers();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Optional<UserDto>> createUser(@Valid @RequestBody RequestUser user) {
        return ResponseEntity.ok(userService.createUser(user));

    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Optional<UserDto>> updateUser(@Valid @RequestBody RequestUser user) {
        return ResponseEntity.ok(userService.updateUser(user));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteUser(@PathVariable int id) {
        userService.deleteUser(id);
    }

    @PutMapping("/{id}/friends/{friendId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<UserDto> addToFriends(@PathVariable int id, @PathVariable int friendId) {
        Optional<UserDto> newFriend = userService.addFriend(friendId, id);
        return newFriend.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.badRequest().build());
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    @ResponseStatus(HttpStatus.OK)
    public void removeFromFriends(@PathVariable int id, @PathVariable int friendId) {
        userService.deleteFriend(friendId, id);
    }

    @GetMapping("/{id}/friends")
    @ResponseStatus(HttpStatus.OK)
    public Optional<List<UserDto>> getAllFriends(@PathVariable int id) {
        return userService.getFriendsUser(id);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Optional<List<UserDto>>> getAllCommonFriends(@PathVariable int id, @PathVariable int otherId) {
        return ResponseEntity.ok(userService.getCommonFriends(id, otherId));
    }
}
