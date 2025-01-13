package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;

public interface FilmStorage {

    Collection<Film> getAllFilms();

    Film getFilm(Long id);

    Film createFilm(Film film);

    Film updateFilm(Film film);

    Film deleteFilm(Film film);

}
