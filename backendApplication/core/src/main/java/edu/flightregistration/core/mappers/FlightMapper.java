package edu.flightregistration.core.mappers;

import edu.flightregistration.core.domain.Flight;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface FlightMapper {

    @Mapping(target = "id", source = "flightNumber")
    edu.flightregistration.core.jpa.entity.Flight domainToEntity(Flight domainFlight);

    @Mapping(target = "flightNumber", source = "id")
    Flight entityToDomain(edu.flightregistration.core.jpa.entity.Flight entity);

    List<Flight> listOfEntityToListOfDomain(List<edu.flightregistration.core.jpa.entity.Flight> entityList);
}
