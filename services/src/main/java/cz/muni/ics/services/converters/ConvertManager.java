package cz.muni.ics.services.converters;

import cz.muni.ics.services.enums.ConvertType;
import cz.muni.ics.services.exceptions.ServiceException;
import cz.muni.ics.services.exceptions.ServiceMessages;
import cz.muni.ics.services.interfaces.ConvertService;
import org.elasticsearch.common.inject.Singleton;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Singleton
@Component
public class ConvertManager {

    private final JsonConvertService jsonConvertService;
    private final PcapConvertService pcapConvertService;

    @Autowired
    public ConvertManager(JsonConvertService jsonConvertService, PcapConvertService pcapConvertService) {
        this.jsonConvertService = jsonConvertService;
        this.pcapConvertService = pcapConvertService;
    }

    public ConvertService getConvertService(ConvertType type) throws ServiceException {
        switch (type){
            case JSON: return jsonConvertService;
            case PCAP: return pcapConvertService;
            default: throw new ServiceException(ServiceMessages.ERROR_UNKNOWN_SERVICE + type);
        }
    }
}
