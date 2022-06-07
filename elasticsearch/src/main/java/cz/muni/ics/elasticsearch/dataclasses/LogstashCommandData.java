package cz.muni.ics.elasticsearch.dataclasses;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@Document(indexName = "#{@appCommandsIndex}", type = "logstash_command_data", shards = 2, replicas = 1)
public class LogstashCommandData {

    @Id
    private String id;

    public LogstashCommandData(List<String> logstashRootCommands) {
        this.logstashRootCommands = logstashRootCommands;
    }

    @NonNull
    @NotEmpty
    private List<String> logstashRootCommands = new ArrayList<>();
}
