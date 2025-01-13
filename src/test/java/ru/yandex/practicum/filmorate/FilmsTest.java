package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertThrows;


@SpringBootTest
public class FilmsTest {

    @Test
    void checkNullName() {
        NullPointerException exception = assertThrows(NullPointerException.class, () -> {
            new Film(null, "description", LocalDate.of(2020, 10, 10), 100L);
        });
    }

    @Test
    void checkNullDescription() {
        NullPointerException exception = assertThrows(NullPointerException.class, () -> {
            new Film("Name", null, LocalDate.of(2020, 10, 10), 100L);
        });
    }

    @Test
    void checkNullReleaseDate() {
        NullPointerException exception = assertThrows(NullPointerException.class, () -> {
            new Film("Name", "description", null, 100L);
        });
    }


    @Test
    void checkNullDuration() {
        NullPointerException exception = assertThrows(NullPointerException.class, () -> {
            new Film("Name", "description", LocalDate.of(2020, 10, 10), null);
        });
    }


}
