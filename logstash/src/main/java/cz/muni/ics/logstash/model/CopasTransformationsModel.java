package cz.muni.ics.logstash.model;

import cz.muni.ics.logstash.commands.nodes.NodeEnumImpl;
import cz.muni.ics.logstash.commands.roots.RootEnumImpl;
import cz.muni.ics.logstash.exception.LogstashException;
import cz.muni.ics.logstash.interfaces.Node;
import cz.muni.ics.logstash.interfaces.NodeEnum;
import cz.muni.ics.logstash.interfaces.Root;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

@Data
@Slf4j
public class CopasTransformationsModel {

    List<Root> roots = new ArrayList<>();

    public CopasTransformationsModel() {
        Root filter = RootEnumImpl.FILTER.getCommandInstance();
        initFilter(filter);
        roots.add(filter);
    }

    private void initFilter(Root filter) {
        Node dns;
        Node timestamp;
        Node fingerprint;
        try {
            dns = NodeEnumImpl.DNS.getCommandInstance();
            timestamp = NodeEnumImpl.TIMESTAMP.getCommandInstance();
            fingerprint = NodeEnumImpl.ANONYMIZE.getCommandInstance();
            filter.add(dns);
            filter.add(timestamp);
            filter.add(fingerprint);
        } catch (LogstashException e) {
            log.error(String.valueOf(e));
        }
    }
}
