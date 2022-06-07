package cz.muni.ics.logstash.commands.commandsconfig;

import cz.muni.ics.logstash.interfaces.LogstashPrintable;
import cz.muni.ics.logstash.interfaces.Root;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class LogstashCommandsConfig extends ArrayList<Root> implements LogstashPrintable {

    @Override
    public String toLogstashString(int level) {
        StringBuilder sb = new StringBuilder();
        this.forEach(command -> sb.append(command.toLogstashString(level)).append("\n"));
        return sb.toString();
    }
}
