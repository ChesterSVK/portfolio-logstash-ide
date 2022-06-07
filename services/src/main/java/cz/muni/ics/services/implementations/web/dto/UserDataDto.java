package cz.muni.ics.services.implementations.web.dto;

import cz.muni.ics.services.validation.PasswordMatches;
import cz.muni.ics.services.validation.ValidEmail;
import lombok.*;
import org.springframework.data.annotation.Id;

import javax.validation.constraints.*;

@Data
@NoArgsConstructor
@RequiredArgsConstructor
@PasswordMatches
public class UserDataDto {

    @Id
    private String id;

    @NonNull
    @NotBlank
    @Size(min = 3, max = 30)
    private String name;

    @NonNull
    @NotBlank
    @Size(min = 3, max = 30)
    private String surname;

    @NonNull
    @NotEmpty
    @ValidEmail
    private String email;

    @NonNull
    @Size(min = 6, max = 30)
    private String password;
    private String matchingPassword;
}
