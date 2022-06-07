package cz.muni.ics.core.os;

import cz.muni.ics.core.dataclasses.ExecutorData;
import cz.muni.ics.core.exceptions.ApplicationException;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * @author Jozef Cibík
 */
public interface SystemCommandExecutor {
    int executeWithExitCode(ExecutorData data) throws IOException;
    String executeWithOutput(ExecutorData data) throws IOException;
}
