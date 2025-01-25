package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.database.GenreDbStorage;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class GenreService {
    private final GenreDbStorage storage;

    public Optional<Genre> getGenreById(int id) {
        log.info("В классе {} запущен метод по получению жанра с id = {}", GenreService.class.getName(), id);
        Genre genre = storage.getGenre(id);
        Validate.validateGenre(genre);
        log.info("Жанр с id = {} успешно прошел валидацию", id);
        return Optional.of(genre);
    }

    public Optional<List<Genre>> getGenres() {
        log.info("В классе {} запущен метод по получению всех жанров", GenreService.class.getName());
        return Optional.of(storage.getGenres());
    }
}
