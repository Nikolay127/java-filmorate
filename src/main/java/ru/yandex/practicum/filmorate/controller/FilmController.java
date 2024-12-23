package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/films")
public class FilmController {

    private static final Logger log = LoggerFactory.getLogger(UserController.class);
    private final Map<Long, Film> films = new HashMap<>();
    private Long filmIdCounter = 0L;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Collection<Film> getFilms() {
        log.info("Вызван метод передачи списка всех фильмов");
        Collection<Film> allFilms = films.values();
        log.info("Сформирован и передан список всех фильмов: {}", allFilms);
        return allFilms;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Film createFilm(@Valid @RequestBody Film film) {
        log.info("Начался процесс создания фильма {}", film);
        validateDataReleaseAndName(film);
        setFilmId(film);
        films.put(film.getId(), film);
        log.info("Процесс создания фильма {} - успешно завершен", film);
        return film;
    }

    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film film) {
        log.info("Начался процесс обновления фильма {}", film);
        validateDataReleaseAndName(film);
        if (!films.containsKey(film.getId())) {
            log.error("Фильм {} не найден", film);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Фильм не найден");
        }
        films.put(film.getId(), film);
        log.info("Процесс обновления фильма {} - успешно завершен", film);
        return film;
    }

    private void validateDataReleaseAndName(Film film) {
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            log.error("Фильм {} не создан, по причине неверной даты релиза", film);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Дата релиза — не раньше 28 декабря 1895 года");
        } else if (film.getName().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Название фильма не может быть пустым");
        }
    }

    private void setFilmId(Film film) {
        film.setId(++filmIdCounter);
    }
}
