package fr.b4.apps.storages.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class DurationDTO {
    @JsonProperty("days")
    private Integer days;
    @JsonProperty("hours")
    private Integer hours;
    @JsonProperty("minutes")
    private Integer minutes;
}
