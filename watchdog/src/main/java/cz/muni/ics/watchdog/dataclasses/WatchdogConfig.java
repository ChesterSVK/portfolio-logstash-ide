package cz.muni.ics.watchdog.dataclasses;

import lombok.*;

import javax.validation.constraints.NotEmpty;

/**
 * Java representation of the config used in watchdog script.
 *
 * @author Jozef Cibík
 */

@Data
@RequiredArgsConstructor
public class WatchdogConfig {

    @NonNull
    @NotEmpty
    @Setter(value = AccessLevel.NONE)
    private String convertType;

    private String host = "";

    @Override
    public String toString() {
        return "WatchdogConfig{" +
                "convertType='" + convertType + '\'' +
                ", host='" + host + '\'' +
                '}';
    }
}
