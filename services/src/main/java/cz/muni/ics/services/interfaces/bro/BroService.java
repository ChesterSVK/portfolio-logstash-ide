package cz.muni.ics.services.interfaces.bro;

import cz.muni.ics.bro.exceptions.BroException;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Collection;

/**
 * This service provides methods ued for processing pcap files by Bro tool
 * see https://www.bro.org/
 *
 * @author Jozef Cibík
 */
public interface BroService {

    Path createCustomConfig(String customCommand, Path outputDir) throws IOException, BroException;

    boolean broProcessPcap(Collection<Path> files, Path broOutputDir, Path config);

    boolean broProcessPcap(Path file, Path broOutputDir, Path config);
}
