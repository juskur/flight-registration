package edu.flightregistration.core.services.async;

import edu.flightregistration.core.domain.Terminal;

import java.util.List;

public interface TerminalService {

    List<Terminal> getAllTerminals();
    String getTerminalNumber(String flightNumber);
}
