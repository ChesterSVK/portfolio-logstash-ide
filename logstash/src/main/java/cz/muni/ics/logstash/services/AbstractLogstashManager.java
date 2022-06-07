package cz.muni.ics.logstash.services;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import cz.muni.ics.logstash.commands.nodes.NodeCommandInstance;
import cz.muni.ics.logstash.commands.nodes.plugins.DnsResolvingCommand;
import cz.muni.ics.logstash.commands.nodes.plugins.FingerprintCommand;
import cz.muni.ics.logstash.commands.nodes.plugins.TimestampCommand;
import cz.muni.ics.logstash.commands.roots.RootCommandInstance;
import cz.muni.ics.logstash.exception.LogstashException;
import cz.muni.ics.logstash.exception.LogstashMessages;
import cz.muni.ics.logstash.interfaces.ILogstashManager;
import cz.muni.ics.logstash.model.ErrorItem;
import cz.muni.ics.logstash.model.ErrorResponse;
import cz.muni.ics.logstash.model.SuccessResponse;
import cz.muni.ics.logstash.serialization.nodes.NodeSerializer;
import cz.muni.ics.logstash.serialization.roots.RootSerializer;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@Slf4j
public abstract class AbstractLogstashManager implements ILogstashManager {

    private Gson gson;

    protected AbstractLogstashManager(){
        gson = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(RootCommandInstance.class, new RootSerializer())
                .registerTypeAdapter(NodeCommandInstance.class, new NodeSerializer())
                .create();
    }

    @Override
    public final void handleRequest(HttpServletRequest request, HttpServletResponse response) {

        final String method = request.getMethod();
        final String mode = request.getParameter("mode");

        Object responseData = null;
        response.setStatus(200);

        try {
            if (mode.isEmpty()) {
                generateResponse(response, LogstashMessages.MODE_ERROR);
                return;
            }
            if (method.equals("GET")) {
                throw new UnsupportedOperationException("Get method in logstash manager");
            } else if (method.equals("POST")) {
                switch (mode) {
                    default:
                        throw new LogstashException(LogstashMessages.MODE_ERROR);
                    case "defaultTransformations":
                        responseData = actionGetDefaultTransformations();
                        break;
                    case "predefinedTransformations":
                        responseData = actionGetPredefinedTransformations();
                        break;
                    case "getRootCommands":
                        responseData = actionGetRootCommands();
                        break;
                    case "getNodeCommands":
                        responseData = actionGetNodeCommands(request);
                        break;
                    case "getLeafCommands":
                        responseData = actionGetLeafCommands(request);
                        break;
                    case "getUsedFunctions":
                        responseData = actionGetUsedFunctions();
                        break;
                    case "deleteFunction":
                        responseData = actionDeleteFunction(request);
                        break;
                    case "addFunction":
                        responseData = actionAddFunction(request);
                        break;
                    case "deleteAllFunctions":
                        responseData = actionRemoveAllFunctions();
                        break;
                }
            }

            if (responseData != null) {
                generateResponse(response, responseData);
            }

        } catch (LogstashException e) {
            log.error(e.getMessage(), e);
            generateErrorResponse(response, e.getMessage(), e.getArguments());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            generateErrorResponse(response, LogstashMessages.ERROR_SERVER_INTERNAL, null);
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////    Override

    @Override
    public Object actionGetRootCommands() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Object actionGetNodeCommands(HttpServletRequest rootCommandName) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Object actionGetLeafCommands(HttpServletRequest nodeCommandName) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Object actionAddFunction(HttpServletRequest request) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Object actionRemoveAllFunctions() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Object actionGetDefaultTransformations() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Object actionGetPredefinedTransformations() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Object actionDeleteFunction(HttpServletRequest request) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Object actionGetUsedFunctions() {
        throw new UnsupportedOperationException();
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////    Private

    private void generateResponse(HttpServletResponse response, Object data) throws IOException {
        response.addHeader("Content-Type", "application/json; charset=utf-8");
        response.setStatus(200);

        response.getWriter().write(gson.toJson(new SuccessResponse(data)));
    }

    private void generateErrorResponse(HttpServletResponse response, String message, List<String> arguments) {
        response.addHeader("Content-Type", "application/json; charset=utf-8");
        response.setStatus(500);

        ErrorItem errorItem = new ErrorItem(message, arguments);

        try {
            response.getWriter().write(gson.toJson(new ErrorResponse(errorItem)));
        } catch (IOException ignore) {
        }
    }
}
