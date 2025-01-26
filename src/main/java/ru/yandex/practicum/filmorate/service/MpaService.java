package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.NotFoundRating;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.database.RatingDbStorage;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class MpaService {
    private final RatingDbStorage storage;

    public Optional<Mpa> getMpaById(int id) {
        log.info("В классе {} запущен метод по получению рейтинга с id = {}", MpaService.class.getName(), id);
        Mpa mpa = storage.getMpa(id);
        String validationError = Validate.validateMpa(mpa);
        if (validationError != null) {
            throw new NotFoundRating(validationError);
        }
        return Optional.of(mpa);
    }

    public Optional<List<Mpa>> getAllMpa() {
        log.info("В классе {} запущен метод по получению всех рейтингов", MpaService.class.getName());
        return Optional.of(storage.getAllMpa());
    }
}
