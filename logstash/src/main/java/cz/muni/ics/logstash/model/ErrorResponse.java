package cz.muni.ics.logstash.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ErrorResponse {

    private List<ErrorItem> errors = new ArrayList<>();

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////    Constructors

    public ErrorResponse(ErrorItem item) {
        errors.add(item);
    }
    public ErrorResponse(List<ErrorItem> items) {
        errors.addAll(items);
    }
}
