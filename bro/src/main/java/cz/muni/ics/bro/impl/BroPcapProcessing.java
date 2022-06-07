package cz.muni.ics.bro.impl;

import cz.muni.ics.bro.exceptions.BroException;
import cz.muni.ics.bro.exceptions.BroMessages;
import cz.muni.ics.bro.utils.BroExecutor;
import cz.muni.ics.core.dataclasses.ExecutorData;
import cz.muni.ics.core.exceptions.ApplicationException;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Collections;

/**
 * Pcap processing executes command to process PCAP files as an input for bro script
 *
 * @author Jozef Cibík
 */

@Component
public class BroPcapProcessing {

    private final BroExecutor executor;

    private static final String TYPE_EXTENSION = "pcap";

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////    Constructor

    @Autowired
    public BroPcapProcessing(BroExecutor executor) {
        this.executor = executor;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////    Public

    public int processInputFile(Path fileToProcess, Path outputDir, Path broCconfig)
            throws BroException, IOException, ApplicationException {
        checkInput(fileToProcess, outputDir, broCconfig);
        return executor.executeCommand(
                createData(outputDir, new String[]{"-r", fileToProcess.toString(), broCconfig.toString()})
        );
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////    Private

    private void checkInput(Path file, Path outputDir, Path config) throws BroException {
        checkOutputDir(outputDir);
        checkExtension(file);
        checkHidden(file);
        checkExists(file);
        checkConfig(config);
    }

    private void checkConfig(Path config) throws BroException {
        if (config == null) {
            throw new BroException(
                    BroMessages.ERROR_NULL_CONFIG);
        }
        if (!config.toFile().exists()) {
            throw new BroException(
                    BroMessages.ERROR_FILE_NOT_EXIST,
                    Collections.singletonList(config.toString()));
        }
    }

    private void checkOutputDir(Path broOutputDir) throws BroException {
        if (!broOutputDir.toFile().exists()) {
            throw new BroException(
                    BroMessages.ERROR_NO_OUTPUT_DIR,
                    Collections.singletonList(broOutputDir.toString()));
        }
        if (!broOutputDir.toFile().isDirectory()) {
            throw new BroException(
                    BroMessages.ERROR_INVALID_OUTPUT_DIR,
                    Collections.singletonList(broOutputDir.toString()));
        }
    }

    private void checkExtension(Path file) throws BroException {
        if (!FilenameUtils.getExtension(file.toString()).toLowerCase().equals(TYPE_EXTENSION.toLowerCase())) {
            throw new BroException(
                    BroMessages.ERROR_INVALID_EXTENSION,
                    Collections.singletonList(FilenameUtils.getExtension(file.toString())));
        }
    }

    private void checkHidden(Path file) throws BroException {
        //File.isHidden() is not working
        //Workaround
        if (file.toFile().getName().startsWith(".")) {
            throw new BroException(
                    BroMessages.ERROR_INPUT_FILE_HIDDEN,
                    Collections.singletonList(file.toString()));
        }
    }

    private void checkExists(Path file) throws BroException {
        if (!file.toFile().exists()) {
            throw new BroException(
                    BroMessages.ERROR_FILE_NOT_EXIST,
                    Collections.singletonList(file.toString()));
        }
    }

    private ExecutorData createData(Path directoryPath, String[] commandsList) {
        ExecutorData data = new ExecutorData(executor.getBroBinPath());
        data.setWorkingDirectory(directoryPath.toFile());
        data.setParams(commandsList);
        return data;
    }
}
