package cz.muni.ics.logstash.services;

import cz.muni.ics.logstash.commands.commandsconfig.LogstashCommandsConfig;
import cz.muni.ics.logstash.commands.commandsconfig.LogstashCommandsConfigManager;
import cz.muni.ics.logstash.commands.leafs.GenericLeafCommand;
import cz.muni.ics.logstash.commands.nodes.NodeEnumImpl;
import cz.muni.ics.logstash.commands.roots.RootEnumImpl;
import cz.muni.ics.logstash.interfaces.Node;
import cz.muni.ics.logstash.interfaces.Root;
import cz.muni.ics.logstash.model.ConfigResponse;
import cz.muni.ics.logstash.model.CopasTransformationsModel;
import cz.muni.ics.logstash.model.LogstashTranformationsModel;
import cz.muni.ics.logstash.serialization.nodes.NodeCommandGsonHelper;
import cz.muni.ics.logstash.serialization.roots.RootCommandGsonHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Component
public class LogstashManagerImpl extends AbstractLogstashManager {

    private LogstashCommandsConfig defaultCommandsConfig;
    private final LogstashCommandsConfigManager commandsConfigManager;
    private String inputFilesType;

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////    Constructors

    @Autowired
    public LogstashManagerImpl(LogstashCommandsConfigManager commandsConfigManager) {
        this.commandsConfigManager = commandsConfigManager;
        this.defaultCommandsConfig = commandsConfigManager.getCopASLogstashConfiguration();
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////    Override

    @Override
    public Object actionAddFunction(HttpServletRequest request) {
        String functionJson = getFunction(request);
        Root root = RootCommandGsonHelper.deserialize(functionJson);
        commandsConfigManager.addRootCommand(root, defaultCommandsConfig);
        return generateConfigResponse(defaultCommandsConfig);
    }

    @Override
    public Object actionRemoveAllFunctions() {
        this.defaultCommandsConfig = commandsConfigManager.getCopASLogstashConfiguration();
        return generateConfigResponse(defaultCommandsConfig);
    }

    @Override
    public Object actionDeleteFunction(HttpServletRequest request) {
        String functionJson = getFunction(request);
        Root root = RootCommandGsonHelper.deserialize(functionJson);
        commandsConfigManager.deleteRootCommand(root, defaultCommandsConfig);
        return generateConfigResponse(defaultCommandsConfig);
    }

    @Override
    public Object actionGetUsedFunctions() {
        return generateConfigResponse(defaultCommandsConfig);
    }

    @Override
    public Object actionGetDefaultTransformations() {
        return new LogstashTranformationsModel().getRoots();
    }

    @Override
    public Object actionGetPredefinedTransformations() {
        return new CopasTransformationsModel().getRoots();
    }

    @Override
    public void parseCommandsToInitialConfiguration(List<String> logstashRootCommands) {
        List<Root> roots = new ArrayList<>();
        logstashRootCommands.forEach(commandString -> {
            roots.add(RootCommandGsonHelper.deserialize(commandString));
        });
        defaultCommandsConfig = commandsConfigManager.createLogstashConfigFromCommands(roots);
    }

    @Override
    public String getConfigurationJson() {
        return defaultCommandsConfig.toLogstashString(0);
    }

    @Override
    public LogstashCommandsConfig getConfiguration() {
        return defaultCommandsConfig;
    }

    @Override
    public void setInputFilesType(String convertFileType) {
        this.inputFilesType = convertFileType;
    }

    @Override
    public String getInputFilesType() {
        return this.inputFilesType;
    }

    @Override
    public Object actionGetRootCommands() {
        return Arrays.stream(RootEnumImpl.values());
    }

    @Override
    public Object actionGetNodeCommands(HttpServletRequest request) {
        Root r = parseRootCommandFromRequest(request);
        List<NodeEnumImpl> nodeList = new ArrayList<>();
        for (NodeEnumImpl c : NodeEnumImpl.values()) {
            if (c.getAllowedParentRoots().contains(RootEnumImpl.valueOf(r.getCommandName()))) {
                nodeList.add(c);
            }
        }
        return nodeList;
    }


    @Override
    public Object actionGetLeafCommands(HttpServletRequest request) {
        Node n = parseNodeCommandFromRequest(request);
        List<GenericLeafCommand> leavesList = new ArrayList<>();
        for (GenericLeafCommand c : GenericLeafCommand.values()) {
            if (c.getAllowedParentNodes().contains(NodeEnumImpl.valueOf(n.getCommandName()))) {
                leavesList.add(c);
            }
        }
        return leavesList;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////    Private

    private Root parseRootCommandFromRequest(HttpServletRequest request) {
        String rootCommandString = request.getParameter("rootCommandName");
        if (rootCommandString == null) {
            throw new IllegalArgumentException();
        }
        return RootCommandGsonHelper.deserialize(rootCommandString);
    }

    private Node parseNodeCommandFromRequest(HttpServletRequest request) {
        String nodeCommandString = request.getParameter("nodeCommandName");
        if (nodeCommandString == null) {
            throw new IllegalArgumentException();
        }
        return NodeCommandGsonHelper.deserialize(nodeCommandString);
    }

    private String getFunction(HttpServletRequest request) {
        String functionJson = request.getParameter("function");
        if (functionJson == null) {
            throw new IllegalArgumentException("Argument 'function' is missing");
        }
        return functionJson;
    }

    private ConfigResponse generateConfigResponse(LogstashCommandsConfig originalConfiguration) {
        return new ConfigResponse(
                originalConfiguration.toLogstashString(0),
                commandsConfigManager.getUsedCommands(originalConfiguration));
    }
}
