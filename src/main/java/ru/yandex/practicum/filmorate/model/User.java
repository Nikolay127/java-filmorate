package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Pattern;
import lombok.*;


import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

/**
 * User
 */

@Data
@NoArgsConstructor
@RequiredArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = {"id"})
public class User {

    @JsonIgnore
    private final Set<User> friends = new HashSet<>();

    protected Long id;

    @NonNull
    @Email(message = "Email must contain a valid '@' symbol.")
    protected String email;

    @NonNull
    @Pattern(regexp = "^\\S+$", message = "Login cannot contain spaces.")
    protected String login;

    @NonNull
    protected String name;

    @NonNull
    @PastOrPresent
    protected LocalDate birthday;
}
