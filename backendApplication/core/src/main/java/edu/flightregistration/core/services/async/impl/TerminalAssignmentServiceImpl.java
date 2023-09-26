package edu.flightregistration.core.services.async.impl;

import com.google.common.base.Strings;
import edu.flightregistration.core.comparators.StringComparator;
import edu.flightregistration.core.domain.FlightStatus;
import edu.flightregistration.core.domain.Terminal;
import edu.flightregistration.core.services.sync.ArrivedFlightsService;
import edu.flightregistration.core.services.sync.FlightService;
import edu.flightregistration.core.services.async.TerminalAssignmentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class TerminalAssignmentServiceImpl implements TerminalAssignmentService {

    private final FlightService flightService;
    private final ArrivedFlightsService arrivedFlightsService;
    private final StringComparator stringComparator;

    @Override
    public synchronized void assignFlight(Terminal terminal) {
        String assignedFlight = terminal.getFlightNumber();
        if (!terminal.isEmpty()) {
            tryUnassignDepartedFlight(terminal);
        }
        if (terminal.isEmpty()) {
            tryAssignNextArrivedFlight(terminal);
        }
        if (!stringComparator.stringsAreEqualIgnoringCase(assignedFlight, terminal.getFlightNumber())) {
            log.info("For terminal {} flight changed from {} to {}",
                    terminal.getTerminalNumber(), assignedFlight, terminal.getFlightNumber());
        }
    }

    private void tryUnassignDepartedFlight(Terminal terminal) {
        if (flightService.getFlightStatus(terminal.getFlightNumber()) == FlightStatus.DEPARTED) {
            terminal.setFlightNumber(null);
        }
    }

    private void tryAssignNextArrivedFlight(Terminal terminal) {
        String flightNumber = arrivedFlightsService.getNextArrivedFlight();
        if (!Strings.isNullOrEmpty(flightNumber)) {
            terminal.setFlightNumber(flightNumber);
        }
    }
}
