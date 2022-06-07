package cz.muni.ics.elasticsearch.dataclasses;

import cz.muni.ics.elasticsearch.utils.LocalDateTimeFormatterUtil;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@RequiredArgsConstructor
@Document(indexName = "#{@appDataIndex}", type = "application_data", shards = 2, replicas = 1)
public class ApplicationData {

    @Id
    private String id;

    @NonNull
    @NotEmpty
    @Field(type = FieldType.Text, includeInParent = true, store = true)
    private String logstashCommandId;

    @NonNull
    @NotEmpty
    private String type;

    @NonNull
    private String figureNote = "";

    @NonNull
    @NotEmpty
    @Setter(AccessLevel.NONE)
    private String dateCreated = LocalDateTimeFormatterUtil.getNormalizedDate(LocalDateTime.now());
}
