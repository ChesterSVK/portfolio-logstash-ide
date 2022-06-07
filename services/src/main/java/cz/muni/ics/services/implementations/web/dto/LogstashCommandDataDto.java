package cz.muni.ics.services.implementations.web.dto;

import lombok.*;
import org.springframework.data.annotation.Id;

import javax.validation.constraints.NotEmpty;
import java.util.List;

@Data
@NoArgsConstructor
@RequiredArgsConstructor
public class LogstashCommandDataDto {
    @Id
    private String id;
    @NonNull
    @NotEmpty
    private List<String> logstashRootCommands;
}
