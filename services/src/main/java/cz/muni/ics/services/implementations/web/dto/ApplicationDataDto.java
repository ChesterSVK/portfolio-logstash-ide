package cz.muni.ics.services.implementations.web.dto;

import cz.muni.ics.services.enums.ConvertType;
import lombok.*;
import org.springframework.data.annotation.Id;

import javax.validation.constraints.NotEmpty;

@Data
@NoArgsConstructor
@RequiredArgsConstructor
public class ApplicationDataDto {
    @Id
    private String id;
    @NonNull
    @NotEmpty
    private String logstashCommandId;
    @NonNull
    private ConvertType type;
    @NonNull
    private String figureNote = "";
    @NonNull
    @NotEmpty
    private String dateCreated;
    private boolean watchFolders = false;
    private boolean saveLogstashConfig = false;
    public ApplicationDataDto(ConvertType convertType) {this.type = convertType;}
}
