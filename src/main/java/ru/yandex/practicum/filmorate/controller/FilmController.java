package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.RequestCreateFilm;
import ru.yandex.practicum.filmorate.model.RequestUpdateFilm;
import ru.yandex.practicum.filmorate.model.dto.FilmDto;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/films")
@RequiredArgsConstructor
public class FilmController {

    private static final Logger log = LoggerFactory.getLogger(FilmController.class);
    private final FilmService filmService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Optional<List<FilmDto>> getFilms() {
        log.info("В контроллере {} запущен метод получения всех фильмов", FilmController.class.getName());
        return filmService.getAllFilms();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Optional<FilmDto> createFilm(@Valid @RequestBody RequestCreateFilm request) {
        log.info("В контроллере {} запущен метод для создания фильма", FilmController.class.getName());
        return filmService.createFilm(request);
    }

    @GetMapping("{filmId}")
    public Optional<FilmDto> getFilmById(@PathVariable("filmId") Integer id) {
        log.info("В контроллере {} запущен метод для получения пользователя с id = {}",
                FilmController.class.getName(),
                id);
        return filmService.getFilmById(id);
    }

    @PutMapping
    public Optional<FilmDto> updateFilm(@Valid @RequestBody RequestUpdateFilm request) {
        log.info("В контроллере {} запущен метод для обновления фильма", FilmController.class.getName());
        return filmService.updateFilm(request);
    }

    @DeleteMapping("/{id}")
    public void deleteFilm(@PathVariable("id") final int filmId) {
        log.info("В контроллере {} запущен метод для удаления пользователя", FilmController.class.getName());
        filmService.deleteFilm(filmId);
    }

    @PutMapping("/{id}/like/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public void addLike(@PathVariable int id, @PathVariable int userId) {
        log.info("В контроллере {} запущен метод для проставления лайка фильму", FilmController.class.getName());
        filmService.addLike(id, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteLike(@PathVariable int id, @PathVariable int userId) {
        log.info("В контроллере {} запущен метод для удаления лайка у фильма", FilmController.class.getName());
        filmService.deleteLike(id, userId);
    }

    @GetMapping("/popular")
    @ResponseStatus(HttpStatus.OK)
    public Optional<List<FilmDto>> getPopularFilms(@RequestParam(defaultValue = "10") String count) {
        log.info("В контроллере {} запущен метод для получения списка популярных фильмов", FilmController.class.getName());
        return filmService.getPopularFilms(count);
    }
}
