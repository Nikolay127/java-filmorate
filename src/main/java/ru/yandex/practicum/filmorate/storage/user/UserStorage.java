package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;

public interface UserStorage {

    Collection<User> getAllUsers();

    User getUser(Long id);

    User createUser(User user);

    User updateUser(User user);

    User deleteUser(User user);

}
