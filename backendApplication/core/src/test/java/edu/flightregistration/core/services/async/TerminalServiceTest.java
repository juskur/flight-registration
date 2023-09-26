package edu.flightregistration.core.services.async;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@Import(TerminalServiceTest.TestConfig.class)
class TerminalServiceTest {

    @Autowired
    TerminalService terminalService;

    @Test
    void canAutowire() {
        assertNotNull(terminalService, "Can not autowire service");
    }

    @Test
    void canGetAllTerminals() {
        var terminals = terminalService.getAllTerminals();
        assertNotNull(terminals, "List of terminals is null");
        assertTrue(terminals.size() > 0, "List of terminals is empty");

        var terminal = terminals.get(0);
        terminal.setFlightNumber("VO000");
    }

    @Configuration
    @ComponentScan(basePackages = {"edu.flightregistration.core"})
    public static class TestConfig {
    }
}