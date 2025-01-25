package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.ID;
import ru.yandex.practicum.filmorate.model.dto.FilmDto;
import ru.yandex.practicum.filmorate.model.dto.UserDto;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Slf4j
public class Validate {
    public static Optional<String> validateUser(UserDto user) {
        log.info("В классе {} запущен метод по валидации пользователя с id = {}",
                Validate.class.getName(),
                user);
        if (user.getEmail() == null || !user.getEmail().contains("@")) {
            log.trace("Введен неправильный формат Email");
            return Optional.of("Электронная почта не может быть пустой и должна содержать символ @");
        }
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
            log.trace("Имя пользователя изменено на логин");
        }
        if (user.getBirthday().isAfter(LocalDate.now())) {
            log.trace("Дата рождения еще не произошла {}", user.getBirthday());
            return Optional.of("Дата рождения не может быть в будущем");
        }
        return Optional.empty();
    }

    public static Optional<String> validateFilm(FilmDto film) {
        log.info("В классе {} запущен метод по валидации фильма с id = {}",
                Validate.class.getName(),
                film);
        if (film.getName() == null || film.getName().isBlank()) {
            log.trace("Не задано название фильма: {}", film.getName());
            return Optional.of("Имя не может быть пустым");
        }
        if (film.getDescription() != null) {
            if (film.getDescription().length() > 200) {
                log.trace("Превышена длинна описания: {}", film.getDescription().length());
                return Optional.of("Максимальная длина описания — 200 символов");
            }
        }
        if (film.getReleaseDate().isBefore(LocalDate.parse("1895-12-28"))) {
            log.trace("Дата релиза не соответствует условию: {}", film.getReleaseDate());
            return Optional.of("Дата релиза — не раньше 28 декабря 1895 года");
        }
        if (film.getDuration() < 0) {
            log.trace("Продолжительность не соответствует условию: {}", film.getDuration());
            return Optional.of("Продолжительность фильма должна быть положительным числом.");
        }
        return Optional.empty();
    }

    public static Optional<String> validateGenre(List<Genre> allGenres, List<ID> genresFilm) {
        log.info("В классе {} запущен метод по поиску жанров в БД", Validate.class.getName());
        List<Integer> listAllId = allGenres.stream().map(Genre::getId).toList();
        for (ID genre : genresFilm) {
            if (!listAllId.contains(genre.getId())) {
                return Optional.of("Жанр не найден в БД");
            }
        }
        return Optional.empty();
    }

    public static Optional<String> validateGenre(Genre genre) {
        log.info("В классе {} запущен метод по валидации жанра {}", Validate.class.getName(), genre);
        if (genre == null) {
            return Optional.of("Жанр не указан");
        }
        return Optional.empty();
    }

    public static Optional<String> validateMpa(Mpa mpa) {
        log.info("В классе {} запущен метод по валидации рейтинга {}", Validate.class.getName(), mpa);
        if (mpa == null) {
            return Optional.of("Рейтинг не указан");
        }
        return Optional.empty();
    }

    public static Optional<String> validateMpa(List<Mpa> allMpa, List<ID> mpaID) {
        log.info("В классе {} запущен метод по поиску рейтингов в БД", Validate.class.getName());
        List<Integer> listAllID = allMpa.stream().map(Mpa::getId).toList();
        for (ID genre : mpaID) {
            if (!listAllID.contains(genre.getId())) {
                return Optional.of("Mpa не найден в БД");
            }
        }
        return Optional.empty();
    }
}
