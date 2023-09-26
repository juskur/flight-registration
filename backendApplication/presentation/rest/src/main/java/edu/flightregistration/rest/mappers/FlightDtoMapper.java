package edu.flightregistration.rest.mappers;

import edu.flightregistration.core.domain.Flight;
import edu.flightregistration.core.domain.FlightStatus;
import edu.flightregistration.rest.dto.FlightDto;
import edu.flightregistration.rest.dto.FlightStatusDto;
import edu.flightregistration.rest.dto.RegisterFlightDto;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface FlightDtoMapper {

    FlightDto modelToDto(Flight flight);
    Flight dtoToModel(RegisterFlightDto registerFlightDto);
    List<FlightDto> modelToDto(List<Flight> flights);
    FlightStatusDto modelToDto(FlightStatus flightStatus);
}
