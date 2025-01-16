package ru.yandex.practicum.filmorate.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
public class FilmService {

    private static final Logger log = LoggerFactory.getLogger(FilmService.class);
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    @Autowired
    public FilmService(FilmStorage filmStorage, UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    public Collection<Film> getAllFilms() {
        log.info("В сервисе {} вызван метод по получению списка всех фильмов", FilmService.class.getName());
        return filmStorage.getAllFilms();
    }

    public Film createFilm(Film film) {
        log.info("В сервисе {} вызван метод по созданию фильма", FilmService.class.getName());
        validateDataReleaseAndName(film);
        log.info("У фильма {} успешно пройдена валидация даты релиза и названия", film);
        log.info("Вызывается метод по созданию фильма {}", film);
        return filmStorage.createFilm(film);
    }

    public Film updateFilm(Film film) {
        log.info("В сервисе {} вызван метод по обновлению фильма", FilmService.class.getName());
        validateDataReleaseAndName(film);
        log.info("У обновляемого фильма {} успешно пройдена валидация даты релиза и названия", film);
        log.info("Вызывается метод по обновлению фильма {}", film);
        return filmStorage.updateFilm(film);
    }

    public Film addLike(Long filmId, Long userId) {
        log.info("Вызван метод проставления лайка фильму с id {} от пользователя с id {}", filmId, userId);
        if (!isFilmExist(filmId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Запрашиваемого фильма нет");
        }
        if (!isUserExist(userId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Запрашиваемого пользователя нет");
        }
        filmStorage.getFilm(filmId).addLike(userId);
//        filmStorage.getFilm(filmId).getLikes().add(userId);
        log.info("Поставлен лайка фильму с id {} от пользователя с id {}", filmId, userId);
        return filmStorage.getFilm(filmId);
    }

    public Film deleteLike(Long filmId, Long userId) {
        log.info("Вызван метод удаления лайка с фильма с id {} от пользователя с id {}", filmId, userId);
        if (!isFilmExist(filmId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Запрашиваемого фильма нет");
        }
        if (!isUserExist(userId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Запрашиваемого пользователя нет");
        }
        filmStorage.getFilm(filmId).removeLike(userId);
//        filmStorage.getFilm(filmId).getLikes().remove(userId);
        log.info("Удален лайк фильму с id {} от пользователя с id {}", filmId, userId);
        return filmStorage.getFilm(filmId);
    }

    public Collection<Film> getPopularFilms(Long id) {
        log.info("Вызван метод получения самых популярных фильмов");
        List<Film> allFilms = new ArrayList<>(filmStorage.getAllFilms());
        // Сортируем фильмы по количеству лайков в порядке убывания
        allFilms.sort((film1, film2) -> Long.compare(film2.getRate(), film1.getRate()));
        // Ограничиваем количество фильмов до указанного числа
        List<Film> popularFilms = allFilms.subList(0, Math.min(id.intValue(), allFilms.size()));
        log.info("Сформирован список популярных фильмов: {}", popularFilms);
        return popularFilms;
    }

    private boolean isFilmExist(Long filmId) {
        return filmStorage.getAllFilms().contains(filmStorage.getFilm(filmId));
    }

    private boolean isUserExist(Long userId) {
        return userStorage.getAllUsers().contains(userStorage.getUser(userId));
    }

    private void validateDataReleaseAndName(Film film) {
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            log.error("Фильм {} не создан, по причине неверной даты релиза", film);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Дата релиза — не раньше 28 декабря 1895 года");
        } else if (film.getName().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Название фильма не может быть пустым");
        }
    }
}
