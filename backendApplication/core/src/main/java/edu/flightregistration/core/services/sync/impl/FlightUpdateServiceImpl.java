package edu.flightregistration.core.services.sync.impl;

import edu.flightregistration.core.exceptions.RegistrationException;
import edu.flightregistration.core.jpa.entity.Flight;
import edu.flightregistration.core.jpa.repository.FlightRepository;
import edu.flightregistration.core.mappers.FlightMapper;
import edu.flightregistration.core.services.sync.ArrivedFlightsService;
import edu.flightregistration.core.services.sync.FlightUpdateService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class FlightUpdateServiceImpl implements FlightUpdateService {

    private final FlightRepository flightRepository;
    private final ArrivedFlightsService arrivedFlightsService;
    private final FlightMapper flightMapper;

    @Override
    public void registerDeparture(String flightNumber) throws RegistrationException {
        var flight = flightRepository.findById(flightNumber);
        checkIfFlightExists(flight, flightNumber);
        setDepartureTimeAndSaveFlight(flight.get());
        arrivedFlightsService.removeArrivedFlight(flightNumber);
    }

    private void checkIfFlightExists(Optional<Flight> flight, String flightNumber) {
        if (flight.isEmpty()) {
            throw new RegistrationException(String.format("Flight with flight number %s not found", flightNumber));
        }
    }

    private void setDepartureTimeAndSaveFlight(Flight flight) {
        flight.setEstimatedDepartureTime(LocalDateTime.now());
        flight.setEstimatedArrivalTime(null);
        saveOrThrowException(flight);
    }

    private void saveOrThrowException(Flight flight) {
        try {
            flightRepository.save(flight);
            log.info("Flight updated {}", flightMapper.entityToDomain(flight));
        } catch (DataAccessException dataAccessException) {
            log.error("Error while saving flight {}", flight, dataAccessException);
            throw new RegistrationException(
                    String.format("Updating of flight %s failed: %s", flight.getId(), dataAccessException.getMessage()));
        }
    }

    @Override
    public void registerArrival(String flightNumber) throws RegistrationException {
        var flight = flightRepository.findById(flightNumber);
        checkIfFlightExists(flight, flightNumber);
        setArrivalTimeAndSaveFlight(flight.get());
        arrivedFlightsService.addArrivedFlight(flightNumber);
    }

    private void setArrivalTimeAndSaveFlight(Flight flight) {
        flight.setEstimatedDepartureTime(null);
        flight.setEstimatedArrivalTime(LocalDateTime.now());
        saveOrThrowException(flight);
    }
}
