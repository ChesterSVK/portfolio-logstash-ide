package cz.muni.ics.core.dataclasses;

import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.NotEmpty;
import java.io.File;
import java.util.Arrays;

@Data
@RequiredArgsConstructor
public class ExecutorData {

    private String input;
    private File workingDirectory;
    private int exitValue = 0;

    @NonNull
    @NotEmpty
    private String command;
    private String[] params = new String[]{};

    public String getInput() {
        return input == null ? input : input + System.lineSeparator();
    }

    @Override
    public String toString() {
        return "ExecutorData{\n" +
                "input= " + input +
                ", \nworkingDirectory= " + workingDirectory +
                ", \nexitValue= " + exitValue +
                ", \ncommand= " + command +
                ", \nparams= " + Arrays.toString(params) +
                "\n}";
    }
}
