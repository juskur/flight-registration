package edu.flightregistration.rest.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AllTerminalsResponseDto {

    @JsonProperty("terminals")
    private List<TerminalDto> terminals;
}
