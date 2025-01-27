package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.yandex.practicum.filmorate.exceptions.*;

@Slf4j
@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler
    public ErrorResponse notFoundFilm(final NotFoundFilmException e) {
        log.info("Перехвачено исключение {}", e.toString());
        return ErrorResponse.create(e, HttpStatus.NOT_FOUND, "Фильм не найден");
    }

    @ExceptionHandler
    public ErrorResponse notFoundUser(final NotFoundUserException e) {
        log.info("Перехвачено исключение {}", e.toString());
        return ErrorResponse.create(e, HttpStatus.NOT_FOUND, "Пользователь не найден");
    }

    @ExceptionHandler
    public ErrorResponse validationErrorParam(final NotValidParamException e) {
        log.info("Перехвачено исключение {}", e.toString());
        return ErrorResponse.create(e, HttpStatus.BAD_REQUEST, "введен неверный параметр");
    }

    @ExceptionHandler
    public ErrorResponse validationException(final ValidationException e) {
        log.info("Перехвачено исключение {}", e.toString());
        return ErrorResponse.create(e, HttpStatus.BAD_REQUEST, e.getReason());
    }

    @ExceptionHandler
    public ErrorResponse handleInternalServerError(final Throwable e) {
        log.info("Перехвачено исключение {}", e.toString());
        return ErrorResponse.create(e, HttpStatus.INTERNAL_SERVER_ERROR, "произошла непредвиденная ошибка");
    }

    @ExceptionHandler
    public ErrorResponse notFoundGenre(final NotFoundGenre e) {
        log.info("Перехвачено исключение {}", e.toString());
        return ErrorResponse.create(e, HttpStatus.NOT_FOUND, "жанр не найден");
    }

    @ExceptionHandler
    public ErrorResponse incorrectGenreID(final IncorrectGenreID e) {
        log.info("Перехвачено исключение {}", e.toString());
        return ErrorResponse.create(e, HttpStatus.NOT_FOUND, "введен некорректный ID жанра");
    }

    @ExceptionHandler
    public ErrorResponse incorrectMpaID(final IncorrectMpaID e) {
        log.info("Перехвачено исключение {}", e.toString());
        return ErrorResponse.create(e, HttpStatus.NOT_FOUND, "введен некорректный ID рейтинга");
    }

    @ExceptionHandler
    public ErrorResponse notFoundMpa(final NotFoundRating e) {
        log.info("Перехвачено исключение {}", e.toString());
        return ErrorResponse.create(e, HttpStatus.NOT_FOUND, "рейтинг не найден");
    }
}