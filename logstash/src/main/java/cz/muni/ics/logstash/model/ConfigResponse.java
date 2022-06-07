package cz.muni.ics.logstash.model;

import cz.muni.ics.logstash.interfaces.Root;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Collection;

@Data
@AllArgsConstructor
public class ConfigResponse {
    private String configString;
    private Collection<Root> commands;
}
