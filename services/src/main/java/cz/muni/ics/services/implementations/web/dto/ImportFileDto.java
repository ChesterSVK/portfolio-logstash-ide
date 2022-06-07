package cz.muni.ics.services.implementations.web.dto;

import com.fabriceci.fmc.model.FileData;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ImportFileDto {
    private boolean watch;
    private FileData fileData;
}
