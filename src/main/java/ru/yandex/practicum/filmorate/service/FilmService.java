package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.*;
import ru.yandex.practicum.filmorate.model.ID;
import ru.yandex.practicum.filmorate.model.RequestCreateFilm;
import ru.yandex.practicum.filmorate.model.RequestUpdateFilm;
import ru.yandex.practicum.filmorate.model.dto.FilmDto;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;
import ru.yandex.practicum.filmorate.storage.database.GenreDbStorage;
import ru.yandex.practicum.filmorate.storage.database.RatingDbStorage;
import ru.yandex.practicum.filmorate.storage.database.response.ResponseFilm;

import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class FilmService {
    @Qualifier("filmDbStorage")
    private final FilmStorage storage;
    @Qualifier("userDbStorage")
    private final UserStorage userStorage;
    private final GenreDbStorage genreStorage;
    private final RatingDbStorage ratingStorage;

    public Optional<FilmDto> createFilm(RequestCreateFilm requestFilm) {
        log.info("В классе {} запущен метод по созданию фильма", FilmService.class.getName());
        Optional<String> validationError = Validate.validateFilm(requestFilm.getFilmDto());
        if (validationError.isPresent()) {
            throw new ValidationException(validationError.get());
        }
        log.info("Валидация фильма {} прошла успешно", requestFilm);
        if (requestFilm.getGenres() != null) {
            Optional<String> validationGenreError = Validate.validateGenre(genreStorage.getGenres(),
                    requestFilm.getGenres());
            if (validationGenreError.isPresent()) {
                throw new NotFoundGenre(validationGenreError.get());
            }
        }
        log.info("Валидация переданного жанра фильма прошла успешно");
        if (requestFilm.getMpa() != null) {
            List<ID> mpaList = new ArrayList<>();
            mpaList.add(requestFilm.getMpa());
            Optional<String> validationMpaError = Validate.validateMpa(ratingStorage.getAllMpa(), mpaList);
            if (validationMpaError.isPresent()) {
                throw new NotFoundRating(validationMpaError.get());
            }
        }
        log.info("Валидация переданного рейтинга фильма прошла успешно");
        ResponseFilm response = storage.createFilm(requestFilm);
        log.info("Фильм {} успешно создан в базе данных", requestFilm);
        return Optional.ofNullable(response.getFilmDto());
    }

    public Optional<FilmDto> getFilmById(int id) {
        log.info("В классе {} запущен метод по получению фильма с id = {}", FilmService.class.getName(), id);
        return Optional.ofNullable(storage.getFilmById(id).getFilmDto());
    }

    public Optional<FilmDto> updateFilm(RequestUpdateFilm request) {
        log.info("В классе {} запущен метод по обновлению фильма с id = {}",
                FilmService.class.getName(),
                request.getId());
        Optional<String> validationError = Validate.validateFilm(request.getFilmDto());
        if (validationError.isPresent()) {
            throw new ValidationException(validationError.get());
        }
        log.info("Фильм с id = {} успешно прошел валидацию", request.getId());
        ResponseFilm response = storage.updateFilm(request.getRequestFilm());
        log.info("Фильм с id = {} успешно прошел обновлён", request.getId());
        return Optional.ofNullable(response.getFilmDto());
    }

    public void deleteFilm(final int filmID) {
        log.info("В классе {} запущен метод по удалению фильма с id = {}", FilmService.class.getName(), filmID);
        storage.deleteFilm(filmID);
    }

    public void addLike(final int filmID, final int userID) {
        log.info("В классе {} запущен метод по проставлению лайка фильму с id = {} от пользователя с id = {}",
                FilmService.class.getName(),
                filmID,
                userID);
        if (!userStorage.existsById(userID)) {
            throw new NotFoundUserException();
        }
        if (!storage.getLikesFilm(filmID).contains(userID)) {
            storage.addLike(filmID, userID);
        }
    }

    public void deleteLike(final int filmID, final int userID) {
        log.info("В классе {} запущен метод по удалению лайка у фильма с id = {} от пользователя с id = {}",
                FilmService.class.getName(),
                filmID,
                userID);
        if (!storage.hasLike(filmID, userID)) {
            throw new NotFoundUserException();
        }
        storage.deleteLike(filmID, userID);
    }

    public Optional<List<FilmDto>> getAllFilms() {
        log.info("В классе {} запущен метод по получению всех фильмов", FilmService.class.getName());
        return Optional.ofNullable(storage.getAllFilms());
    }

    public Optional<List<FilmDto>> getPopularFilms(Integer countStr) {
        log.info("В классе {} запущен метод по получению {} популярных фильмов", FilmService.class.getName(), countStr);
        return Optional.of(storage.getPopularFilms(countStr));
    }
}
