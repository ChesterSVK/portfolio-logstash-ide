package cz.muni.ics.logstash.commands.commandsconfig;

import cz.muni.ics.logstash.commands.leafs.categories.ElasticsearchLeaf;
import cz.muni.ics.logstash.commands.leafs.categories.JsonLeaf;
import cz.muni.ics.logstash.commands.leafs.categories.RubyLeaf;
import cz.muni.ics.logstash.commands.nodes.NodeEnumImpl;
import cz.muni.ics.logstash.commands.roots.RootEnumImpl;
import cz.muni.ics.logstash.commands.roots.RootHandler;
import cz.muni.ics.logstash.exception.LogstashException;
import cz.muni.ics.logstash.interfaces.Leaf;
import cz.muni.ics.logstash.interfaces.Node;
import cz.muni.ics.logstash.interfaces.Root;
import cz.muni.ics.logstash.types.LArray;
import cz.muni.ics.logstash.types.LString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Slf4j
@Component
public class LogstashCommandsConfigManagerImpl implements LogstashCommandsConfigManager {

    @Value("${default.output.host}")
    private String host;

    private final RootHandler rootHandler;

    @Autowired
    public LogstashCommandsConfigManagerImpl(RootHandler rootHandler) {
        this.rootHandler = rootHandler;
    }

    @Override
    public void addRootCommand(Root newRootCommand, LogstashCommandsConfig commandsConfig) {
        if (!commandsConfig.contains(newRootCommand)) {
            commandsConfig.add(newRootCommand);
        } else {
            rootHandler.addItems(commandsConfig.get(commandsConfig.indexOf(newRootCommand)),newRootCommand.getCommandArgument());
        }
    }

    @Override
    public LogstashCommandsConfig getCopASLogstashConfiguration() {
        LogstashCommandsConfig configuration = new LogstashCommandsConfig();
        initInput(configuration);
        initFilters(configuration);
        initOutput(configuration);
        return configuration;
    }

    @Override
    public Collection<Root> getUsedCommands(LogstashCommandsConfig configuration) {
        return Collections.unmodifiableCollection(configuration);
    }

    @Override
    public void deleteRootCommand(Root rootToDelete, LogstashCommandsConfig originalConfiguration) {
        //If empty nodes delete whole root
        if (rootToDelete.getCommandArgument().isEmpty()){
            originalConfiguration.remove(rootToDelete);
        }
        int index = originalConfiguration.indexOf(rootToDelete);
        if (index == -1)
            return;

        Root r = originalConfiguration.get(index);
        rootToDelete.getCommandArgument().forEach(r::remove);
        if (r.getCommandArgument().isEmpty()) {
            originalConfiguration.remove(r);
        }
    }

    @Override
    public LogstashCommandsConfig createLogstashConfigFromCommands(List<Root> roots) {
        LogstashCommandsConfig configuration = new LogstashCommandsConfig();
        configuration.addAll(roots);
        return configuration;
    }


    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////    Private
    //////////////////////////////////////////////////////////////////////////////////////////////////////    Init roots

    private void initInput(LogstashCommandsConfig configuration) {
        Root input = RootEnumImpl.INPUT.getCommandInstance();
        initInputRoot(input);
        addRootCommand(input, configuration);
    }

    private void initOutput(LogstashCommandsConfig configuration) {
        Root output = RootEnumImpl.OUTPUT.getCommandInstance();
        initOutputRoot(output);
        addRootCommand(output, configuration);
    }

    private void initFilters(LogstashCommandsConfig configuration) {
        Root filter = RootEnumImpl.FILTER.getCommandInstance();
        initFilterRoot(filter);
        addRootCommand(filter, configuration);
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////    Init nodes

    private void initInputRoot(Root input) {
        Node stdIn;
        try {
            stdIn = NodeEnumImpl.STDIN.getCommandInstance();
            input.add(stdIn);
        } catch (LogstashException e) { log.error(String.valueOf(e)); }
    }

    private void initOutputRoot(Root output) {
        Node es;
        try {
            es = NodeEnumImpl.ELASTICSEARCH.getCommandInstance();
            initEsNode(es);
            output.add(es);
        } catch (LogstashException | IllegalAccessException | InstantiationException e) { log.error(String.valueOf(e)); }
    }

    private void initFilterRoot(Root filter) {
        Node json;
        try {
            json = NodeEnumImpl.JSON.getCommandInstance();
            initJsonNode(json);
            filter.add(json);
        } catch (LogstashException | IllegalAccessException | InstantiationException e) { log.error(String.valueOf(e)); }
        Node ruby;
        try {
            ruby = NodeEnumImpl.RUBY.getCommandInstance();
            initRubyNode(ruby);
            filter.add(ruby);
        } catch (LogstashException | InstantiationException | IllegalAccessException e) { log.error(String.valueOf(e)); }
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////    Init leaves

    private void initEsNode(Node es) throws InstantiationException, IllegalAccessException {
        Leaf hosts = ElasticsearchLeaf.HOSTS.getCommandInstance();
        LArray array = new LArray();
        array.add(new LString(this.host));
        hosts.setCommandArgument(array);
        es.add(hosts);
    }

    private void initJsonNode(Node json) throws InstantiationException, IllegalAccessException {
        Leaf source = JsonLeaf.SOURCE.getCommandInstance();
        source.setCommandArgument(new LString("message"));
        json.add(source);
    }

    private void initRubyNode(Node ruby) throws InstantiationException, IllegalAccessException {
        Leaf code = RubyLeaf.CODE.getCommandInstance();
        code.setCommandArgument(new LString("event.to_hash.keys.each { |k| event.set(k.sub('.','-'), event.remove(k)) if k.include?'.' }"));
        ruby.add(code);
    }
}
