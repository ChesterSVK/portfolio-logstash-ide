package cz.muni.ics.watchdog.dataclasses;

import cz.muni.ics.watchdog.enums.WatchdogAction;
import lombok.*;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.Set;

/**
 * Dataclass represents an host entity used in invoking watchdog script with specific parameters that are provided by
 * this dataclass.
 *
 * @author Jozef Cibík
 */

@Data
@AllArgsConstructor
public class WatchdogData {

    @NonNull
    @NotEmpty
    @Setter(value = AccessLevel.NONE)
    private String scriptPath;

    @NonNull
    @NotEmpty
    @Setter(value = AccessLevel.NONE)
    private String configPath;

    @NonNull
    @Min(1)
    @Max(60)
    private Integer minutes = 60;

    @NonNull
    private WatchdogAction action = WatchdogAction.REGISTER;

    public WatchdogData(@NonNull @NotEmpty String scriptPath, @NonNull @NotEmpty String configPath) {
        this.scriptPath = scriptPath;
        this.configPath = configPath;
    }

    @NonNull
    private Set<Path> foldersToWatch = new HashSet<>();
}
