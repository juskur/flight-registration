package edu.flightregistration.core.services.sync.impl;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import edu.flightregistration.core.domain.Flight;
import edu.flightregistration.core.domain.FlightStatus;
import edu.flightregistration.core.exceptions.RegistrationException;
import edu.flightregistration.core.jpa.repository.FlightRepository;
import edu.flightregistration.core.mappers.FlightMapper;
import edu.flightregistration.core.services.sync.FlightService;
import edu.flightregistration.core.validators.FlightValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class FlightServiceImpl implements FlightService {

    private final FlightMapper flightMapper;
    private final FlightRepository flightRepository;
    private final FlightValidator flightValidator;

    @Override
    public void registerFlight(Flight flight) throws RegistrationException, IllegalArgumentException {
        flightValidator.validateFlightForRegistration(flight);
        var entityFlight = flightMapper.domainToEntity(flight);
        saveOrThrowException(flight, entityFlight);
    }

    private void saveOrThrowException(Flight flight, edu.flightregistration.core.jpa.entity.Flight entityFlight) {
        try {
            flightRepository.save(entityFlight);
            log.info("Flight registered {}", flight);
        } catch (DataAccessException dataAccessException) {
            log.error("Error while saving flight {}", flight, dataAccessException);
            throw new RegistrationException(
                    String.format("Registration of flight %s failed: %s", flight.toString(), dataAccessException.getMessage()));
        }
    }

    @Override
    public Flight getRegisteredFlight(String flightNumber) {
        flightValidator.validateFlightExists(flightNumber);
        return flightMapper.entityToDomain(
                flightRepository.findById(flightNumber).get()
        );
    }

    @Override
    public FlightStatus getFlightStatus(String flightNumber) throws RegistrationException {
        return getRegisteredFlight(flightNumber)
                .getFlightStatus();
    }

    @Override
    public List<Flight> getAllRegisteredFlights() {
        return flightMapper.listOfEntityToListOfDomain(
                flightRepository.findAll());
    }

    @Override
    public List<Flight> findFlightsByOriginOrDestination(String origin, String destination) {
        Preconditions.checkArgument(!Strings.isNullOrEmpty(origin) && !Strings.isNullOrEmpty(destination),
                "Origin and destination can not be empty");
        return flightMapper.listOfEntityToListOfDomain(
                flightRepository.findByOriginContainingOrDestinationContaining(origin, destination));
    }

    @Override
    public List<Flight> findFlightsByOriginAndDestination(String origin, String destination) {
        Preconditions.checkArgument(!Strings.isNullOrEmpty(origin) && !Strings.isNullOrEmpty(destination),
                "Origin and destination can not be empty");
        return flightMapper.listOfEntityToListOfDomain(
                flightRepository.findByOriginContainingAndDestinationContaining(origin, destination));
    }
}
