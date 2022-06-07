package cz.muni.ics.logstash.model;

import cz.muni.ics.logstash.commands.leafs.categories.*;
import cz.muni.ics.logstash.commands.nodes.NodeEnumImpl;
import cz.muni.ics.logstash.commands.roots.RootEnumImpl;
import cz.muni.ics.logstash.exception.LogstashException;
import cz.muni.ics.logstash.interfaces.Node;
import cz.muni.ics.logstash.interfaces.Root;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@Data
@Slf4j
public class LogstashTranformationsModel {

    List<Root> roots = new ArrayList<>();

    public LogstashTranformationsModel() {
        Root input = RootEnumImpl.INPUT.getCommandInstance();
        initInput(input);
        Root filter = RootEnumImpl.FILTER.getCommandInstance();
        initFilter(filter);
        Root output = RootEnumImpl.OUTPUT.getCommandInstance();
        initOutput(output);

        roots.add(input);
        roots.add(filter);
        roots.add(output);
    }

    private void initInput(Root input) {
        Node stdin;
        try {
            stdin = NodeEnumImpl.STDIN.getCommandInstance();
            input.add(stdin);
        } catch (LogstashException e) {
            log.error(String.valueOf(e));
        }
//        initStdin(stdin);
    }

    private void initFilter(Root filter) {
        Node mutate;
        Node date;
        Node fingerprint;
        Node ruby;
        Node json;
        try {
            mutate = NodeEnumImpl.MUTATE.getCommandInstance();
            date = NodeEnumImpl.DATE.getCommandInstance();
            fingerprint = NodeEnumImpl.FINGERPRINT.getCommandInstance();
            ruby = NodeEnumImpl.RUBY.getCommandInstance();
            json = NodeEnumImpl.JSON.getCommandInstance();
            initMutate(mutate);
            initDate(date);
            initFingerPrint(fingerprint);
            initRuby(ruby);
            initJson(json);
            filter.add(mutate);
            filter.add(date);
            filter.add(fingerprint);
            filter.add(ruby);
            filter.add(json);
        } catch (LogstashException | IllegalAccessException | InstantiationException e) {
            log.error(String.valueOf(e));
        }
    }

    private void initJson(Node json) throws InstantiationException, IllegalAccessException {
        addCommonOptions(json);

        json.add(JsonLeaf.SOURCE.getCommandInstance());
        json.add(JsonLeaf.SKIP_ON_INVALID_JSON.getCommandInstance());
        json.add(JsonLeaf.TAG_ON_FAILURE.getCommandInstance());
        json.add(JsonLeaf.TARGET.getCommandInstance());
    }

    private void initRuby(Node ruby) throws InstantiationException, IllegalAccessException {
        addCommonOptions(ruby);

        ruby.add(RubyLeaf.CODE.getCommandInstance());
    }

    private void initFingerPrint(Node fingerprint) throws InstantiationException, IllegalAccessException {
        addCommonOptions(fingerprint);

        fingerprint.add(FingerprintLeaf.METHOD.getCommandInstance());
        fingerprint.add(FingerprintLeaf.KEY.getCommandInstance());
        fingerprint.add(FingerprintLeaf.SOURCE.getCommandInstance());
        fingerprint.add(FingerprintLeaf.TARGET.getCommandInstance());
    }

    private void initDate(Node date) throws InstantiationException, IllegalAccessException {
        addCommonOptions(date);
        date.add(DateLeaf.MATCH.getCommandInstance());
        date.add(DateLeaf.LOCALE.getCommandInstance());
        date.add(DateLeaf.TAG_ON_FAILURE.getCommandInstance());
        date.add(DateLeaf.TARGET.getCommandInstance());
        date.add(DateLeaf.TIMEZONE.getCommandInstance());
    }

    private void initMutate(Node mutate) throws InstantiationException, IllegalAccessException {
        addCommonOptions(mutate);

        mutate.add(MutateLeaf.LOWERCASE.getCommandInstance());
        mutate.add(MutateLeaf.UPPERCASE.getCommandInstance());
    }

    private void addCommonOptions(Node node) throws InstantiationException, IllegalAccessException {
        node.add(CommonsLeaf.ADD_FIELD.getCommandInstance());
        node.add(CommonsLeaf.ADD_TAG.getCommandInstance());
        node.add(CommonsLeaf.ENABLE_METRIC.getCommandInstance());
        node.add(CommonsLeaf.ID.getCommandInstance());
        node.add(CommonsLeaf.PERIODIC_FLUSH.getCommandInstance());
        node.add(CommonsLeaf.REMOVE_FIELD.getCommandInstance());
        node.add(CommonsLeaf.REMOVE_TAG.getCommandInstance());
    }

    private void initOutput(Root output) {
        Node elasticsearch;
        try {
            elasticsearch = NodeEnumImpl.ELASTICSEARCH.getCommandInstance();
            initElasticsearch(elasticsearch);
            output.add(elasticsearch);
        } catch (LogstashException | IllegalAccessException | InstantiationException e) {
            log.error(String.valueOf(e));
        }
    }

    private void initElasticsearch(Node elasticsearch) throws InstantiationException, IllegalAccessException {
        elasticsearch.add(ElasticsearchLeaf.HOSTS.getCommandInstance());
    }
}
