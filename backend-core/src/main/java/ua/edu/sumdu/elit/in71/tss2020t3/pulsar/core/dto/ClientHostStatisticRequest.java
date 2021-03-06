package ua.edu.sumdu.elit.in71.tss2020t3.pulsar.core.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.ZonedDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * A POJO that represents a GET request of
 * {@link ua.edu.sumdu.elit.in71.tss2020t3.pulsar.core.entities.client.ClientHostStatistic}
 * associated with exact
 * {@link ua.edu.sumdu.elit.in71.tss2020t3.pulsar.core.entities.client.ClientHost}
 * */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClientHostStatisticRequest {

    /**
     * A public key associated with destination
     * {@link ua.edu.sumdu.elit.in71.tss2020t3.pulsar.core.entities.client.ClientHost}
     * */
    @JsonProperty(value = "public_key", required = true)
    private String publicKey;

    @JsonProperty(value = "start_date")
    private ZonedDateTime startDate;

    @JsonProperty(value = "end_date")
    private ZonedDateTime endDate;
}
