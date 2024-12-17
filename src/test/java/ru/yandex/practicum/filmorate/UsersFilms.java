package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class UsersFilms {

    @Test
    void checkNullEmail() {
        NullPointerException exception = assertThrows(NullPointerException.class, () -> {
            new User(null, "Nick", "", LocalDate.of(1996, 7, 9));
        });
    }

}
