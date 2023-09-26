package edu.flightregistration.core.services.async.impl;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import edu.flightregistration.core.domain.Terminal;
import edu.flightregistration.core.services.async.TerminalAssignmentService;
import edu.flightregistration.core.services.async.TerminalService;
import edu.flightregistration.core.workers.TerminalWorker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class TerminalServiceImpl implements TerminalService {

    public static final String[] terminalLetters = new String[]{"A", "B", "C", "D", "E"};
    private final List<Terminal> terminals;
    private final TerminalAssignmentService terminalAssignmentService;
    private final boolean startWorkers;
    private final ApplicationContext context;

    public TerminalServiceImpl(
            TerminalAssignmentService terminalAssignmentService,
            @Value("${terminal-service.start-workers:true}") boolean startWorkers,
            ApplicationContext context) {
        this.terminalAssignmentService = terminalAssignmentService;
        this.startWorkers = startWorkers;
        this.context = context;
        this.terminals = Collections.unmodifiableList(initializeTerminals());
    }

    private List<Terminal> initializeTerminals() {
        return Arrays.stream(terminalLetters)
                .map(Terminal::new)
                .collect(Collectors.toCollection(LinkedList::new));
    }

    @EventListener
    public void handleContextRefresh(ContextRefreshedEvent event) {
        if (!startWorkers) return;
        log.info("Starting terminal workers");
        for (Terminal terminal : terminals) {
            context.getAutowireCapableBeanFactory().initializeBean(
                    new TerminalWorker(terminal, terminalAssignmentService), "terminalWorker" + terminal.getTerminalNumber());
        }
    }

    @Override
    public List<Terminal> getAllTerminals() {
        return terminals;
    }

    @Override
    public String getTerminalNumber(String flightNumber) {
        Preconditions.checkArgument(!Strings.isNullOrEmpty(flightNumber), "Flight number is null or empty");
        for(Terminal terminal: getAllTerminals()) {
            if (flightNumber.equalsIgnoreCase(terminal.getFlightNumber())) {
                return terminal.getTerminalNumber();
            }
        }
        return null;
    }
}
