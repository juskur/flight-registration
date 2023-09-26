package edu.flightregistration.core.services.sync.impl;

import edu.flightregistration.core.services.sync.ArrivedFlightsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.LinkedList;

@Service
@Slf4j
public class ArrivedFlightServiceImpl implements ArrivedFlightsService {

    private final LinkedList<String> arrivingFlights = new LinkedList<>();

    @Override
    public void addArrivedFlight(String flightNumber) {
        synchronized (arrivingFlights) {
            if (!arrivingFlights.contains(flightNumber)) {
                addFlightToArrivingFlightsAndLog(flightNumber);
            }
        }
    }

    private void addFlightToArrivingFlightsAndLog(String flightNumber) {
        boolean added = arrivingFlights.add(flightNumber);
        if (added) {
            log.info("Flight {} added to arrived and waiting flights list", flightNumber);
        }
    }

    @Override
    public void removeArrivedFlight(String flightNumber) {
        synchronized (arrivingFlights) {
            boolean removed = arrivingFlights.remove(flightNumber);
            if (removed) {
                log.info("Flight {} removed from arrived and waiting flights list", flightNumber);
            }
        }
    }

    @Override
    public String getNextArrivedFlight() {
        synchronized (arrivingFlights) {
            if (arrivingFlights.isEmpty()) {
                return null;
            }
            return arrivingFlights.poll();
        }
    }
}
