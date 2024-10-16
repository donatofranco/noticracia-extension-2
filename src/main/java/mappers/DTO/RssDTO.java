package mappers.DTO;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class RssDTO {
    @JsonProperty("channel")
    public ChannelDTO channel;
}
