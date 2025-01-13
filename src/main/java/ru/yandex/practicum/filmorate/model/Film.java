package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

/**
 * Film
 */
@Data
@NoArgsConstructor
@RequiredArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = {"id"})
public class Film {

    //Сет лайков фильма из id пользователей
    @JsonIgnore
    private final Set<Long> likes = new HashSet<>();

    protected Long id;

    @NonNull
    protected String name;

    @NonNull
    @Size(max = 200)
    protected String description;

    @NonNull
    protected LocalDate releaseDate;

    @NonNull
    @Positive
    protected Long duration;


}
