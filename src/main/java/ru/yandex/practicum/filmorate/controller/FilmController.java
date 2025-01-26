package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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

    private final FilmService filmService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Optional<List<FilmDto>> getFilms() {
        return filmService.getAllFilms();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Optional<FilmDto> createFilm(@Valid @RequestBody RequestCreateFilm request) {
        return filmService.createFilm(request);
    }

    @GetMapping("{filmId}")
    public Optional<FilmDto> getFilmById(@PathVariable("filmId") Integer id) {
        return filmService.getFilmById(id);
    }

    @PutMapping
    public Optional<FilmDto> updateFilm(@Valid @RequestBody RequestUpdateFilm request) {
        return filmService.updateFilm(request);
    }

    @DeleteMapping("/{id}")
    public void deleteFilm(@PathVariable("id") final int filmId) {
        filmService.deleteFilm(filmId);
    }

    @PutMapping("/{id}/like/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public void addLike(@PathVariable int id, @PathVariable int userId) {
        filmService.addLike(id, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteLike(@PathVariable int id, @PathVariable int userId) {
        filmService.deleteLike(id, userId);
    }

    @GetMapping("/popular")
    @ResponseStatus(HttpStatus.OK)
    public List<FilmDto> getPopularFilms(@RequestParam(defaultValue = "10") Integer count) {
        return filmService.getPopularFilms(count);
    }
}
