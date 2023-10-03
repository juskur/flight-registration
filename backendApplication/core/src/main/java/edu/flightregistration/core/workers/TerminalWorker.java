package edu.flightregistration.core.workers;

import edu.flightregistration.core.domain.Terminal;
import edu.flightregistration.core.services.async.TerminalAssignmentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;

@Slf4j
@RequiredArgsConstructor
public class TerminalWorker {

    private final Terminal terminal;
    private final TerminalAssignmentService terminalAssignmentService;

    @Scheduled(fixedDelayString = "${terminal-service.worker-rate:200}")
    public void assignFlightToTerminal() {
        terminalAssignmentService.assignFlight(terminal);
    }
}
