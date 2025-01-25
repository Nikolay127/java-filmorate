package ru.yandex.practicum.filmorate.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.yandex.practicum.filmorate.exceptions.*;

@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler
    public ErrorResponse notFoundFilm(final NotFoundFilmException e) {
        return ErrorResponse.create(e, HttpStatus.NOT_FOUND, "Фильм не найден");
    }

    @ExceptionHandler
    public ErrorResponse notFoundUser(final NotFoundUserException e) {
        return ErrorResponse.create(e, HttpStatus.NOT_FOUND, "Пользователь не найден");
    }

    @ExceptionHandler
    public ErrorResponse validationErrorParam(final NotValidParamException e) {
        return ErrorResponse.create(e, HttpStatus.BAD_REQUEST, "введен неверный параметр");
    }

    @ExceptionHandler
    public ErrorResponse validationException(final ValidationException e) {
        return ErrorResponse.create(e, HttpStatus.BAD_REQUEST, e.getReason());
    }

    @ExceptionHandler
    public ErrorResponse handleInternalServerError(final Throwable e) {
        return ErrorResponse.create(e, HttpStatus.INTERNAL_SERVER_ERROR, "произошла непредвиденная ошибка");
    }

    @ExceptionHandler
    public ErrorResponse notFoundGenre(final NotFoundGenre e) {
        return ErrorResponse.create(e, HttpStatus.NOT_FOUND, "жанр не найден");
    }

    @ExceptionHandler
    public ErrorResponse incorrectGenreID(final IncorrectGenreID e) {
        return ErrorResponse.create(e, HttpStatus.BAD_REQUEST, "введен некорректный ID жанра");
    }

    @ExceptionHandler
    public ErrorResponse incorrectMpaID(final IncorrectMpaID e) {
        return ErrorResponse.create(e, HttpStatus.BAD_REQUEST, "введен некорректный ID рейтинга");
    }

    @ExceptionHandler
    public ErrorResponse notFoundMpa(final NotFoundRating e) {
        return ErrorResponse.create(e, HttpStatus.NOT_FOUND, "рейтинг не найден");
    }
}