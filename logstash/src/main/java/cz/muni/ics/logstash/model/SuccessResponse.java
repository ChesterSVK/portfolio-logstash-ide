package cz.muni.ics.logstash.model;

import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class SuccessResponse {
    @NonNull
    private Object data;
}
