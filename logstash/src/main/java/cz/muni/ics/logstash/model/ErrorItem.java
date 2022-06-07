package cz.muni.ics.logstash.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ErrorItem {

    private String id;
    private int code;
    private String title;
    private List<String> meta;

    public ErrorItem(String message, List<String> arguments) {
        this.id = "server";
        this.code = 500;
        this.title = message;
        if (arguments != null) {
            this.meta = new ArrayList<>();
            this.meta.addAll(arguments);
        }
    }
}
