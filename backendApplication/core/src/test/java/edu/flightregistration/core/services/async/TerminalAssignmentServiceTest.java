package edu.flightregistration.core.services.async;

import edu.flightregistration.core.domain.Flight;
import edu.flightregistration.core.domain.Terminal;
import edu.flightregistration.core.services.sync.FlightService;
import edu.flightregistration.core.services.sync.FlightUpdateService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Import(TerminalAssignmentServiceTest.TestConfig.class)
class TerminalAssignmentServiceTest {

    @Autowired
    TerminalAssignmentService terminalAssignmentService;

    @Autowired
    FlightService flightService;

    @Autowired
    FlightUpdateService flightUpdateService;

    @Test
    void canAutowire() {
        assertNotNull(terminalAssignmentService, "Can not autowire service");
    }

    @Test
    void testDoesNotAssignFlightToTerminalWhenFlightNotRegisteredAsArrived() {

        var flightNumber = "TAS001";
        var flight = new Flight();
        flight.setFlightNumber(flightNumber);
        flight.setAircraftType("Airbus A320 (twin-jet)");
        flight.setOrigin("Antalija AYT");
        flight.setDestination("Vilnius VNO");

        flightService.registerFlight(flight);

        var terminal = new Terminal("A0");

        terminalAssignmentService.assignFlight(terminal);

        assertNull(terminal.getFlightNumber(), "Flight assigned to terminal though it has not arrived");

    }

    @Test
    void testAssignFlightToTerminalWhenFlightRegisteredAsArrived() {

        var flightNumber = "TAS002";
        var flight = new Flight();
        flight.setFlightNumber(flightNumber);
        flight.setAircraftType("Airbus A320 (twin-jet)");
        flight.setOrigin("Antalija AYT");
        flight.setDestination("Vilnius VNO");

        flightService.registerFlight(flight);
        flightUpdateService.registerArrival(flightNumber);

        var terminal = new Terminal("A0");

        terminalAssignmentService.assignFlight(terminal);

        assertEquals(flightNumber, terminal.getFlightNumber(), "Flight not assigned to terminal though it has arrived");

    }

    @Test
    void testAssignFlightToTerminalWhenTwoFlightsRegisteredAsArrived() {

        var flight1Number = "TAS003";
        var flight1 = new Flight();
        flight1.setFlightNumber(flight1Number);
        flight1.setAircraftType("Airbus A320 (twin-jet)");
        flight1.setOrigin("Antalija AYT");
        flight1.setDestination("Vilnius VNO");

        flightService.registerFlight(flight1);
        flightUpdateService.registerArrival(flight1Number);

        var flight2Number = "TAS004";
        var flight2 = new Flight();
        flight2.setFlightNumber(flight2Number);
        flight2.setAircraftType("Airbus A320 (twin-jet)");
        flight2.setOrigin("Antalija AYT");
        flight2.setDestination("Vilnius VNO");

        flightService.registerFlight(flight2);
        flightUpdateService.registerArrival(flight2Number);

        var terminal1 = new Terminal("A0");
        var terminal2 = new Terminal("A1");

        terminalAssignmentService.assignFlight(terminal1);
        terminalAssignmentService.assignFlight(terminal2);

        assertEquals(flight1Number, terminal1.getFlightNumber(), "Flight 1 not assigned to terminal 1 though it has arrived");
        assertEquals(flight2Number, terminal2.getFlightNumber(), "Flight 2 not assigned to terminal 2 though it has arrived");

    }

    @Test
    void testAssignFlightToTerminalOnlyFlightRegisteredAsArrived() {

        var flight1Number = "TAS005";
        var flight1 = new Flight();
        flight1.setFlightNumber(flight1Number);
        flight1.setAircraftType("Airbus A320 (twin-jet)");
        flight1.setOrigin("Antalija AYT");
        flight1.setDestination("Vilnius VNO");

        flightService.registerFlight(flight1);
        flightUpdateService.registerArrival(flight1Number);

        var flight2Number = "TAS006";
        var flight2 = new Flight();
        flight2.setFlightNumber(flight2Number);
        flight2.setAircraftType("Airbus A320 (twin-jet)");
        flight2.setOrigin("Antalija AYT");
        flight2.setDestination("Vilnius VNO");

        flightService.registerFlight(flight2);
        flightUpdateService.registerDeparture(flight2Number);

        var terminal1 = new Terminal("A0");
        var terminal2 = new Terminal("A1");

        terminalAssignmentService.assignFlight(terminal1);
        terminalAssignmentService.assignFlight(terminal2);

        assertEquals(flight1Number, terminal1.getFlightNumber(), "Flight 1 not assigned to terminal 1 though it has arrived");
        assertNull(terminal2.getFlightNumber(), "Flight assigned to terminal 2 though there are no more registered arrivals");

    }

    @Test
    void testUnasignedDepartedFlight() {

        var flightNumber = "TAS007";
        var flight = new Flight();
        flight.setFlightNumber(flightNumber);
        flight.setAircraftType("Airbus A320 (twin-jet)");
        flight.setOrigin("Antalija AYT");
        flight.setDestination("Vilnius VNO");

        flightService.registerFlight(flight);
        flightUpdateService.registerArrival(flightNumber);

        var terminal = new Terminal("A0");

        terminalAssignmentService.assignFlight(terminal);

        assertEquals(flightNumber, terminal.getFlightNumber(), "Flight not assigned to terminal though it has arrived");

        flightUpdateService.registerDeparture(flightNumber);

        terminalAssignmentService.assignFlight(terminal);

        assertNull(terminal.getFlightNumber(), "Flight still assigned to terminal though it has departed");
    }

    @Configuration
    @ComponentScan(basePackages = {"edu.flightregistration.core"})
    public static class TestConfig {
    }
}