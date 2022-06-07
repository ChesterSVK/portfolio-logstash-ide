package com.fabriceci.fmc.error;

import java.util.List;

public class FileManagerException extends Exception {

    private List<String> arguments;

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////    Constructor

    public FileManagerException(String message) { super(message); }
    public FileManagerException(String message, Throwable cause) { super(message, cause); }
    public FileManagerException(String message, List<String> arguments) { super(message); this.arguments = arguments; }
    public FileManagerException(String message, List<String> arguments, Throwable cause) { super(message, cause); this.arguments = arguments; }

    public List<String> getArguments() {
        return arguments;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////    Override

    @Override
    public String toString() {
        String exceptionName = getClass().getName();
        Throwable cause = getCause();
        return cause == null ? exceptionName : createMessage(cause.getLocalizedMessage(), exceptionName);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////    Private

    private String createMessage(String causeMessage, String exceptionName) {
        String res = exceptionName + ": " + super.getLocalizedMessage() +"\n" + causeMessage;
        return (arguments != null && !arguments.isEmpty()) ? (res + "; Args: " + arguments) : res;
    }
}
