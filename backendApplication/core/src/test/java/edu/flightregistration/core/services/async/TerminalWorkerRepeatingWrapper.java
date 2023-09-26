package edu.flightregistration.core.services.async;

import com.google.common.base.Preconditions;
import edu.flightregistration.core.domain.Terminal;
import edu.flightregistration.core.workers.TerminalWorker;

public class TerminalWorkerRepeatingWrapper {

    public static final String TERMINAL_NUMBER_PREFIX = "A";
    private final TerminalWorker terminalWorker;
    private final int repetitions;
    private final long sleepTime;

    public TerminalWorkerRepeatingWrapper(TerminalWorker terminalWorker, int repetitions, long sleepTime) {
        Preconditions.checkArgument(repetitions > 0, "Repetitions can not be less than 1");
        Preconditions.checkArgument(sleepTime >= 0, "Sleep can not be negative");
        this.terminalWorker = terminalWorker;
        this.repetitions = repetitions;
        this.sleepTime = sleepTime;
    }

    public void run() {
        for (int i = 0; i < repetitions; i++) {
            terminalWorker.assignFlightToTerminal();
            try {
                Thread.currentThread().sleep(sleepTime);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    public static TerminalWorkerRepeatingWrapper createTerminalWorkerWrapper(
            int terminalNumberSuffix,
            TerminalAssignmentService terminalAssignmentService,
            int repetitions,
            long sleepTime) {
        Terminal terminal = new Terminal(TERMINAL_NUMBER_PREFIX + terminalNumberSuffix);
        TerminalWorker terminalWorker = new TerminalWorker(terminal, terminalAssignmentService);
        return new TerminalWorkerRepeatingWrapper(terminalWorker, repetitions, sleepTime);
    }
}
