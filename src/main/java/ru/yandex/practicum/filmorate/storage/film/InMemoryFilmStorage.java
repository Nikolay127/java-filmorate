package ru.yandex.practicum.filmorate.storage.film;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Component
public class InMemoryFilmStorage implements FilmStorage {

    private static final Logger log = LoggerFactory.getLogger(InMemoryFilmStorage.class);
    private final Map<Long, Film> films = new HashMap<>();
    private Long filmIdCounter = 0L;

    @Override
    public Collection<Film> getAllFilms() {
        log.info("Вызван метод передачи списка всех фильмов");
        Collection<Film> allFilms = films.values();
        log.info("Сформирован и передан список всех фильмов: {}", allFilms);
        return allFilms;
    }

    public Film getFilm(Long id) {
        log.info("В хранилище {} вызван метод по получению фильма", InMemoryFilmStorage.class.getName());
        return films.get(id);
    }

    @Override
    public Film createFilm(Film film) {
        log.info("Начался процесс создания фильма {}", film);
        setFilmId(film);
        films.put(film.getId(), film);
        log.info("Процесс создания фильма {} - успешно завершен", film);
        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        log.info("Начался процесс обновления фильма {}", film);
        if (!films.containsKey(film.getId())) {
            log.error("Фильм {} не найден", film);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Фильм не найден");
        }
        films.put(film.getId(), film);
        log.info("Процесс обновления фильма {} - успешно завершен", film);
        return film;
    }

    @Override
    public Film deleteFilm(Film film) {
        log.info("Начался процесс удаления фильма {}", film);
        if (!films.containsKey(film.getId())) {
            log.error("Фильм {} не найден", film);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Фильм не найден");
        }
        films.remove(film.getId());
        log.info("Процесс удаления фильма {} - успешно завершен", film);
        return film;
    }

    private void setFilmId(Film film) {
        film.setId(++filmIdCounter);
    }
}
