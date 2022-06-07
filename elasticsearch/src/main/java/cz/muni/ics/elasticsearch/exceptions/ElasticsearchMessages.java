package cz.muni.ics.elasticsearch.exceptions;

import java.util.ResourceBundle;

public final class ElasticsearchMessages {
    private ElasticsearchMessages() {}

    private static final ResourceBundle appBundle = ResourceBundle.getBundle("elasticsearch_messages");

    public static final String ERROR_APPLICATION_DATA_NULL = appBundle.getString("ERROR_APPLICATION_DATA_NULL");
    public static final String ERROR_LOGSTAS_DATA_NULL = appBundle.getString("ERROR_LOGSTASH_DATA_NULL");
    public static final String ERROR_STRING_NULL = appBundle.getString("ERROR_STRING_NULL");
    public static final String ERROR_STRING_EMPTY = appBundle.getString("ERROR_STRING_EMPTY");
}
