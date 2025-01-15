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

    //Сет id пользователей, которые поставили лайк
    @JsonIgnore
    private Set<Long> likesByUsers = new HashSet<>();
    //Чистый рейтинг фильмов, без привязки к пользователям
    @JsonIgnore
    private Long rate = 0L;

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

    public void addLike(Long userId) {
        likesByUsers.add(userId);
        rate++;
    }

    public void removeLike(Long userId) {
        likesByUsers.remove(userId);
        rate--;
    }

}
