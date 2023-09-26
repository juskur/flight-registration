package edu.flightregistration.core.services.async;

import edu.flightregistration.core.domain.Terminal;

public interface TerminalAssignmentService {

    void assignFlight(Terminal terminal);
}
