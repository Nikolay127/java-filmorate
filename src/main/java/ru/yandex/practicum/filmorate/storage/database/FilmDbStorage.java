package ru.yandex.practicum.filmorate.storage.database;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSourceUtils;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exceptions.ErrorAddingData;
import ru.yandex.practicum.filmorate.exceptions.IncorrectGenreID;
import ru.yandex.practicum.filmorate.exceptions.IncorrectMpaID;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.ID;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.RequestCreateFilm;
import ru.yandex.practicum.filmorate.model.dto.FilmDto;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.database.mappers.film.IDRowMapper;
import ru.yandex.practicum.filmorate.storage.database.mappers.film.LikesRowMapper;
import ru.yandex.practicum.filmorate.storage.database.mappers.film.ResponseFilmRowMapper;
import ru.yandex.practicum.filmorate.storage.database.response.ResponseFilm;

import java.util.*;

@Slf4j
@Qualifier("filmDbStorage")
@Repository
@RequiredArgsConstructor
public class FilmDbStorage implements FilmStorage {
    private final NamedParameterJdbcTemplate jdbc;
    private final ResponseFilmRowMapper responseMapper;
    private final GenreDbStorage genreDbStorage;
    private final RatingDbStorage ratingDbStorage;
    private final LikesRowMapper likesMapper;
    private final IDRowMapper idRowMapper;

    @Override
    public ResponseFilm createFilm(RequestCreateFilm request) {
        log.info("В классе {} запущен метод по созданию фильма {}", FilmDbStorage.class.getName(), request);
        SqlParameterSource[] batch = SqlParameterSourceUtils.createBatch(request.getFilmDto());
        String sqlQuery = "INSERT INTO films (name,description,releaseDate,duration) VALUES (:name,:description,:releaseDate,:duration);";
        Integer filmId;
        KeyHolder keyHolder = new GeneratedKeyHolder();
        int[] result;
        try {
            result = jdbc.batchUpdate(sqlQuery, batch, keyHolder);
            filmId = Objects.requireNonNull(keyHolder.getKey()).intValue();
            if (result.length == 0) {
                throw new ErrorAddingData("Данные не были добавлены");
            }
        } catch (DataAccessException e) {
            log.debug("Ошибка при добавлении фильма: {}", e.getMessage());
            throw new ErrorAddingData(e.getMessage());
        }
        if (Objects.nonNull(request.getMpa())) {
            if (Objects.isNull(ratingDbStorage.getMpa(request.getMpa().getId()))) {
                throw new IncorrectMpaID("Неверный id mpa");
            }
            ratingDbStorage.addMpa(filmId, request.getMpa().getId());
        }
        List<ID> genres = request.getGenres();
        if (Objects.nonNull(genres)) {
            for (ID id : genres) {
                if (Objects.isNull(genreDbStorage.getGenre(id.getId()))) {
                    throw new IncorrectGenreID("Неверный id жанра");
                }
                genreDbStorage.addGenreToFilm(filmId, id.getId());
            }
        }
        return getFilmById(filmId);
    }

    @Override
    public ResponseFilm updateFilm(RequestCreateFilm request) {
        log.info("В классе {} запущен метод по обновлению фильма {}", FilmDbStorage.class.getName(), request);
        SqlParameterSource[] batch = SqlParameterSourceUtils.createBatch(request.getFilmDto());
        String sqlQuery = "UPDATE films " +
                "SET name = :name, description = :description, releaseDate = :releaseDate, duration = :duration " +
                "WHERE film_id = " + request.getId();
        int[] result;
        try {
            result = jdbc.batchUpdate(sqlQuery, batch);
        } catch (DataAccessException e) {
            log.debug(e.getMessage());
            throw new RuntimeException(e);
        }

        if (result.length == 0) {
            log.debug("Ошибка при обновлении фильма");
            throw new ErrorAddingData("Данные не были обновлены");
        }
        return getFilmById(request.getId());
    }

    @Override
    public void addLike(int filmID, int userID) {
        SqlParameterSource namedParameters = new MapSqlParameterSource()
                .addValue("filmID", filmID).addValue("userID", userID);
        String sqlQuery = "INSERT INTO likes (film_id,user_id) VALUES (:filmID,:userID)";
        try {
            jdbc.update(sqlQuery, namedParameters);
        } catch (DataAccessException e) {
            log.debug("Ошибка при добавлении лайка: {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteLike(int filmID, int userID) {
        SqlParameterSource namedParameters = new MapSqlParameterSource()
                .addValue("filmID", filmID).addValue("userID", userID);
        String sqlQuery = "DELETE FROM likes WHERE film_id = :filmID AND user_id = :userID";
        try {
            jdbc.update(sqlQuery, namedParameters);
        } catch (DataAccessException e) {
            log.debug("Ошибка при удалении лайка: {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteFilm(int filmID) {
        log.info("В классе {} запущен метод по удалению фильма с id = {}", FilmDbStorage.class.getName(), filmID);
        SqlParameterSource namedParameters = new MapSqlParameterSource()
                .addValue("filmID", filmID);
        String sqlQuery = "DELETE FROM films WHERE film_id = :filmID";
        try {
            jdbc.update(sqlQuery, namedParameters);
        } catch (DataAccessException e) {
            log.debug("Ошибка при удалении фильма: {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public ResponseFilm getFilmById(int filmID) {
        log.info("В классе {} запущен метод по получению фильма с id = {}", FilmDbStorage.class.getName(), filmID);
        SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("id", filmID);
        String sqlQuery = "SELECT * FROM films WHERE film_id = :id;";
        ResponseFilm responseFilm;
        try {
            responseFilm = jdbc.queryForObject(sqlQuery, namedParameters, responseMapper);
        } catch (DataAccessException e) {
            log.debug("Ошибка при получении фильма из БД: {}", e.getMessage());
            throw new RuntimeException(e);
        }
        assert Objects.nonNull(responseFilm);
        Mpa rating = ratingDbStorage.getMpaFilm(filmID);
        List<Genre> genre = genreDbStorage.getGenreFilm(filmID);
        responseFilm.setGenres(genre);
        responseFilm.setRating(rating);
        return responseFilm;
    }

    @Override
    public List<Integer> getLikesFilm(int filmID) {
        log.info("В классе {} запущен метод по получению лайков для фильма с id = {}",
                FilmDbStorage.class.getName(),
                filmID);
        SqlParameterSource namedParameters = new MapSqlParameterSource()
                .addValue("filmID", filmID);
        String sqlQuery = "SELECT film_id, user_id FROM likes WHERE film_id = :filmID";
        List<Integer> likes;
        try {
            likes = jdbc.query(sqlQuery, namedParameters, likesMapper);
        } catch (DataAccessException e) {
            log.debug("Ошибка при получении списка лайков из БД: {}", e.getMessage());
            throw new RuntimeException(e);
        }
        return likes;
    }

    @Override
    public List<FilmDto> getAllFilms() {
        log.info("В классе {} запущен метод по получению списка всех фильмов", FilmDbStorage.class.getName());
        String sqlQuery = "SELECT film_id,name,description,releaseDate,duration FROM films;";
        try {
            return jdbc.query(sqlQuery, responseMapper).stream()
                    .peek(responseFilm -> responseFilm.setRating(ratingDbStorage.getMpaFilm(responseFilm.getId())))
                    .peek(responseFilm -> responseFilm.setGenres(genreDbStorage.getGenreFilm(responseFilm.getId())))
                    .map(ResponseFilm::getFilmDto).toList();
        } catch (DataAccessException e) {
            log.debug("Ошибка при формировании списка фильмов из БД: {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public Map<Integer, List<Integer>> getLikesFilms() {
        String getFilmId = "SELECT film_id FROM films;";
        Map<Integer, List<Integer>> response = new HashMap<>();
        try {
            Set<Integer> idFilms = new HashSet<>(new HashSet<>(jdbc.query(getFilmId, idRowMapper)));
            for (Integer idFilm : idFilms) {
                response.put(idFilm, getLikesFilm(idFilm));
            }
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
        return response;
    }

    @Override
    public List<FilmDto> getPopularFilms(int count) {
        log.info("В классе {} вызван метод получения популярных фильмов, лимит: {}", FilmDbStorage.class.getName(), count);
        String sqlQuery = """
        SELECT f.film_id, f.name, f.description, f.releaseDate, f.duration
        FROM films f
        LEFT JOIN (
            SELECT film_id, COUNT(user_id) AS like_count
            FROM likes
            GROUP BY film_id
        ) l ON f.film_id = l.film_id
        ORDER BY COALESCE(l.like_count, 0) DESC
        LIMIT :count
    """;
        SqlParameterSource namedParameters = new MapSqlParameterSource()
                .addValue("count", count);
        return jdbc.query(sqlQuery, namedParameters, (rs, rowNum) -> FilmDto.builder()
                .id(rs.getInt("film_id"))
                .name(rs.getString("name"))
                .description(rs.getString("description"))
                .releaseDate(rs.getDate("releaseDate").toLocalDate())
                .duration(rs.getInt("duration"))
                .build());
    }

    @Override
    public boolean hasLike(int filmID, int userID) {
        log.info("В классе {} запущен метод проверки лайка пользователя {} для фильма {}", FilmDbStorage.class.getName(), userID, filmID);
        SqlParameterSource namedParameters = new MapSqlParameterSource()
                .addValue("filmID", filmID)
                .addValue("userID", userID);
        String sqlQuery = "SELECT COUNT(*) FROM likes WHERE film_id = :filmID AND user_id = :userID";
        Integer count = jdbc.queryForObject(sqlQuery, namedParameters, Integer.class);
        return count != null && count > 0;
    }
}