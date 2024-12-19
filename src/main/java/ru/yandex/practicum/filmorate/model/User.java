package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Pattern;
import lombok.*;


import java.time.LocalDate;

/**
 * User
 */

@Data
@NoArgsConstructor
@RequiredArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = {"id"})
public class User {

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
