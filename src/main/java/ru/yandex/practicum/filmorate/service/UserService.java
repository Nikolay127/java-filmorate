package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.NotFoundUserException;
import ru.yandex.practicum.filmorate.model.RequestUser;
import ru.yandex.practicum.filmorate.model.dto.UserDto;
import ru.yandex.practicum.filmorate.storage.UserStorage;
import ru.yandex.practicum.filmorate.storage.database.response.ResponseUser;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    @Qualifier("userDbStorage")
    private final UserStorage storage;

    public Optional<UserDto> createUser(RequestUser request) {
        log.info("В классе {} запущен метод по созданию пользователя", UserService.class.getName());
        Validate.validateUser(request.getUserDto());
        UserDto newUser = storage.createUser(request).getUserDto();
        log.info("Добавлен новый пользователь, id пользователя: {}", newUser.getId());
        return Optional.of(newUser);
    }

    public void deleteUser(final int userID) {
        log.info("В классе {} запущен метод по удалению пользователя с id = {}", UserService.class.getName(), userID);
        storage.deleteUser(userID);
        log.info("Пользователь удален, id пользователя: id {} ", userID);
    }

    public Optional<UserDto> updateUser(RequestUser request) {
        log.info("В классе {} запущен метод по обновлению пользователя с id = {}",
                UserService.class.getName(),
                request.getId());
        Validate.validateUser(request.getUserDto());
        log.info("Пользователь с id = {} успешно прошел валидацию", request.getId());
        if (storage.getUserById(request.getId()) == null) {
            throw new NotFoundUserException();
        }
        ResponseUser response = storage.updateUser(request);
        log.info("Данные пользователя обновлены, id пользователя: {}", request.getId());
        return Optional.of(response.getUserDto());
    }

    public Optional<List<UserDto>> getAllUsers() {
        log.info("В классе {} запущен метод по получению всех пользователей", UserService.class.getName());
        return Optional.of(storage.getAllUsers().stream().map(ResponseUser::getUserDto).toList());
    }

    public Optional<UserDto> getUserByID(final int userID) {
        log.info("В классе {} запущен метод по получению пользователя с id = {}", UserService.class.getName(), userID);
        return Optional.of(storage.getUserById(userID).getUserDto());
    }

    public Optional<UserDto> addFriend(final int newFriendID, final int userID) {
        log.info("В классе {} запущен метод по добавлению в друзья пользователя с id = {} пользователю с id = {}",
                UserService.class.getName(),
                newFriendID,
                userID);
        if (!storage.getListFriends(userID).contains(newFriendID)) {
            storage.addFriend(userID, newFriendID);
            log.info("Добавление в друзья пользователя с id = {} пользователю с id = {} успешно завершено",
                    newFriendID,
                    userID);
            log.info("Список друзей пользователя {} : {}", userID, storage.getListFriends(userID));
            return Optional.of(storage.getUserById(newFriendID).getUserDto());
        }
        return Optional.empty();
    }

    public void deleteFriend(final int friendID, final int userID) {
        log.info("В классе {} запущен метод по удалению из друзей пользователя с id = {} у пользователя с id = {}",
                UserService.class.getName(),
                friendID,
                userID);
        if (storage.getUserById(friendID) == null || storage.getUserById(userID) == null) {
            throw new NotFoundUserException();
        }
        storage.deleteFriend(userID, friendID);
        log.info("Удаление из друзей пользователя с id = {} у пользователя с id = {} успешно завершено",
                friendID,
                userID);
    }

    public Optional<List<UserDto>> getFriendsUser(final int userID) {
        log.info("В классе {} запущен метод на получение списка друзей пользователя с id = {}",
                UserService.class.getName(),
                userID);
        ResponseUser response = storage.getUserById(userID);
        if (response == null) {
            throw new NotFoundUserException();
        }
        return Optional.of(storage.getListFriends(userID).stream()
                .map(storage::getUserById)
                .map(ResponseUser::getUserDto)
                .toList());
    }

    public Optional<List<UserDto>> getCommonFriends(final int friendID, final int userID) {
        log.info("В классе {} запущен метод на получение списка общих друзей пользователя с id = {} с пользователем" +
                        "с id = {}",
                UserService.class.getName(),
                friendID,
                userID);
        final List<Integer> friendListUserOne = storage.getListFriends(friendID);
        final List<Integer> friendListUserTwo = storage.getListFriends(userID);
        return Optional.of(friendListUserOne.stream()
                .filter(friendListUserTwo::contains)
                .map(storage::getUserById)
                .map(ResponseUser::getUserDto)
                .toList());
    }
}
