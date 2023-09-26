package edu.flightregistration.rest.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class TerminalDto implements Serializable {

    @JsonProperty("terminalNumber")
    private String terminalNumber;

    @JsonProperty("flightNumber")
    private String flightNumber;
}
