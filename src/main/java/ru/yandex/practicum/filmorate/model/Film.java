package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDate;

/**
 * Film
 */
@Data
@NoArgsConstructor
@RequiredArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = {"id"})
public class Film {

    protected Long id;

    @NonNull
    @NotBlank(message = "Email cannot be empty.")
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
