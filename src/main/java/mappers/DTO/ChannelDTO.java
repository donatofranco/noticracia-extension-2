package mappers.DTO;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ChannelDTO {
    @JsonProperty("item")
    @JacksonXmlElementWrapper(useWrapping = false)
    public List<ItemDTO> items;
}
