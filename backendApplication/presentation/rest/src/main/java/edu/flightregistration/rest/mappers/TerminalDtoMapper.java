package edu.flightregistration.rest.mappers;

import edu.flightregistration.rest.dto.TerminalDto;
import edu.flightregistration.core.domain.Terminal;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TerminalDtoMapper {

    TerminalDto modelToDto(Terminal terminal);
    List<TerminalDto> modelToDto(List<Terminal> terminals);
}
