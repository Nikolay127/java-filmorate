package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.ID;
import ru.yandex.practicum.filmorate.model.dto.FilmDto;
import ru.yandex.practicum.filmorate.model.dto.UserDto;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class Validate {
    public static String validateUser(UserDto user) {
        log.info("В классе {} запущен метод по валидации пользователя с id = {}",
                Validate.class.getName(),
                user);
        List<String> errors = new ArrayList<>();
        if (user.getEmail() == null || !user.getEmail().contains("@")) {
            log.trace("Введен неправильный формат Email");
            errors.add("Электронная почта не может быть пустой и должна содержать символ @");
        }
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
            log.trace("Имя пользователя изменено на логин");
        }
        if (user.getBirthday().isAfter(LocalDate.now())) {
            log.trace("Дата рождения еще не произошла {}", user.getBirthday());
            errors.add("Дата рождения не может быть в будущем");
        }
        return errors.isEmpty() ? null : String.join("; ", errors);
    }

    public static String validateFilm(FilmDto film) {
        log.info("В классе {} запущен метод по валидации фильма с id = {}",
                Validate.class.getName(), film);
        List<String> errors = new ArrayList<>();
        if (film.getName() == null || film.getName().isBlank()) {
            log.trace("Не задано название фильма: {}", film.getName());
            errors.add("Имя не может быть пустым");
        }
        if (film.getDescription() != null && film.getDescription().length() > 200) {
            log.trace("Превышена длина описания: {}", film.getDescription().length());
            errors.add("Максимальная длина описания — 200 символов");
        }
        if (film.getReleaseDate().isBefore(LocalDate.parse("1895-12-28"))) {
            log.trace("Дата релиза не соответствует условию: {}", film.getReleaseDate());
            errors.add("Дата релиза — не раньше 28 декабря 1895 года");
        }
        if (film.getDuration() < 0) {
            log.trace("Продолжительность не соответствует условию: {}", film.getDuration());
            errors.add("Продолжительность фильма должна быть положительным числом.");
        }
        return errors.isEmpty() ? null : String.join("; ", errors);
    }


    public static String validateGenre(List<Genre> allGenres, List<ID> genresFilm) {
        log.info("В классе {} запущен метод по поиску жанров в БД", Validate.class.getName());
        List<Integer> listAllId = allGenres.stream().map(Genre::getId).toList();
        for (ID genre : genresFilm) {
            if (!listAllId.contains(genre.getId())) {
                return "Жанр не найден в БД";
            }
        }
        return null;
    }

    public static String validateGenre(Genre genre) {
        log.info("В классе {} запущен метод по валидации жанра {}", Validate.class.getName(), genre);
        if (genre == null) {
            return "Жанр не указан";
        }
        return null;
    }

    public static String validateMpa(Mpa mpa) {
        log.info("В классе {} запущен метод по валидации рейтинга {}", Validate.class.getName(), mpa);
        if (mpa == null) {
            return "Рейтинг не указан";
        }
        return null;
    }

    public static String validateMpa(List<Mpa> allMpa, List<ID> mpaID) {
        log.info("В классе {} запущен метод по поиску рейтингов в БД", Validate.class.getName());
        List<Integer> listAllID = allMpa.stream().map(Mpa::getId).toList();
        for (ID genre : mpaID) {
            if (!listAllID.contains(genre.getId())) {
                return "Mpa не найден в БД";
            }
        }
        return null;
    }
}
