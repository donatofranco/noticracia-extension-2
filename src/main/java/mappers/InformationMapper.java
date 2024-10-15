package mappers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import mappers.DTO.RssDTO;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class InformationMapper {

    private final XmlMapper xmlMapper = new XmlMapper();

    public Map<String, String> mapInformation(String xmlResponse) {
        return parseXml(xmlResponse)
                .map(this::extractInformation)
                .orElseGet(Collections::emptyMap);
    }

    private Optional<RssDTO> parseXml(String xmlResponse) {
        try {
            return Optional.ofNullable(xmlMapper.readValue(xmlResponse, RssDTO.class));
        } catch (JsonProcessingException e) {
            System.err.println("Error al leer XML: " + e.getMessage());
            return Optional.empty();
        }
    }

    private Map<String, String> extractInformation(RssDTO rss) {
        Map<String, String> informationMap = new HashMap<>();
        if (rss != null && rss.channel != null && rss.channel.items != null) {
            rss.channel.items.forEach(item -> informationMap.put(item.link, item.title));
        }
        return Collections.unmodifiableMap(informationMap);
    }
}
