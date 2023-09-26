package edu.flightregistration.core.services.async;

import edu.flightregistration.core.domain.Flight;
import edu.flightregistration.core.domain.Terminal;
import edu.flightregistration.core.services.sync.ArrivedFlightsService;
import edu.flightregistration.core.services.sync.FlightService;
import edu.flightregistration.core.services.sync.FlightUpdateService;
import edu.flightregistration.core.workers.TerminalWorker;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

@SpringBootTest
@Import(TerminalAssignmentServiceTestAsync.TestConfig.class)
@Slf4j
public class TerminalAssignmentServiceTestAsync {

    @Autowired
    TerminalAssignmentService terminalAssignmentService;

    @Autowired
    FlightService flightService;

    @Autowired
    FlightUpdateService flightUpdateService;

    @Autowired
    ArrivedFlightsService arrivedFlightsService;

    @Autowired
    TerminalService terminalService;

    @Test
    public void testCanAssignFlightsWhenManyFlights() throws InterruptedException {
        var numberOfTerminals = 5;
        var numberOfFlights = 10;
        var service = Executors.newFixedThreadPool(numberOfTerminals + numberOfFlights);
        var terminalsLatch = new CountDownLatch(numberOfTerminals);
        var arrivalsLatch = new CountDownLatch(numberOfFlights);
        var terminals = new ArrayList<Terminal>();
        //Start terminal workers
        for (int i = 0; i < numberOfTerminals; i++) {
            Terminal terminal = new Terminal("A" + i);
            terminals.add(terminal);
            TerminalWorker terminalWorker = new TerminalWorker(terminal, terminalAssignmentService);
            var terminalWorkerWrapper
                    = new TerminalWorkerRepeatingWrapper(terminalWorker, 10, 100);
            service.execute(() -> {
                terminalWorkerWrapper.run();
                terminalsLatch.countDown();
            });
        }
        //Start arrivals
        var flights = new ArrayList<Flight>();
        for (int i = 0; i < numberOfFlights; i++) {
            var flight = new Flight();
            flight.setFlightNumber("DDD" + i);
            flight.setAircraftType("Airbus A320 (twin-jet)");
            flight.setOrigin("Antalija AYT");
            flight.setDestination("Vilnius VNO");
            flights.add(flight);
            service.execute(() -> {
                flightService.registerFlight(flight);
                flightUpdateService.registerArrival(flight.getFlightNumber());
                arrivalsLatch.countDown();
            });
        }
        arrivalsLatch.await();
        log.info("Flights arrived");
        sleepHandleInterruptedException(200);

        //Start departures of first 5 flights
        var departuresLatch = new CountDownLatch(numberOfFlights);
        for (int i = 0; i < numberOfFlights - 5; i++) {
            var flight = flights.get(i);
            service.execute(() -> {
                flightUpdateService.registerDeparture(flight.getFlightNumber());
                departuresLatch.countDown();
            });
        }
        arrivalsLatch.await();
        log.info("Flights departed");

        //Wait for terminal workers to finish
        terminalsLatch.await();

        String actualNextFlight = arrivedFlightsService.getNextArrivedFlight();
        assertNull(actualNextFlight, "Arrived flight left not taken by terminal");
        terminals.forEach(
                terminal -> assertNotNull(terminal.getFlightNumber(), "Flight number is null though five flights not departed"));
    }


    @Test
    public void testCanAssignFlightsWhenFlightsArriveAndDepart() throws InterruptedException {
        var numberOfThreads = 5;
        var numberOfFlights = 5;
        var service = Executors.newFixedThreadPool(numberOfThreads + numberOfFlights);
        var latch = new CountDownLatch(numberOfThreads);
        var terminals = new ArrayList<Terminal>();
        //Start terminal workers
        for (int i = 0; i < numberOfThreads; i++) {
            Terminal terminal = new Terminal("A" + i);
            terminals.add(terminal);
            TerminalWorker terminalWorker = new TerminalWorker(terminal, terminalAssignmentService);
            var terminalWorkerWrapper
                    = new TerminalWorkerRepeatingWrapper(terminalWorker, 10, 100);
            service.execute(() -> {
                terminalWorkerWrapper.run();
                latch.countDown();
            });
        }
        //Start arrivals departures
        for (int i = 0; i < numberOfFlights; i++) {
            var flight = new Flight();
            flight.setFlightNumber("CCC" + i);
            flight.setAircraftType("Airbus A320 (twin-jet)");
            flight.setOrigin("Antalija AYT");
            flight.setDestination("Vilnius VNO");
            service.execute(() -> {
                flightService.registerFlight(flight);
                flightUpdateService.registerArrival(flight.getFlightNumber());
                sleepHandleInterruptedException(200);
                flightUpdateService.registerDeparture(flight.getFlightNumber());
            });
        }
        //Wait for terminal workers to finish
        latch.await();

        String actualNextFlight = arrivedFlightsService.getNextArrivedFlight();
        assertNull(actualNextFlight, "Arrived flight left not taken by terminal");
        terminals.forEach(
                terminal -> assertNull(terminal.getFlightNumber(), "Flight number is not null though all flights departed"));
    }

    @Test
    public void testCanAssignFlightsWhenFlightsArrive() throws InterruptedException {
        var numberOfThreads = 5;
        var numberOfFlights = 5;
        var service = Executors.newFixedThreadPool(numberOfThreads + numberOfFlights);
        var latch = new CountDownLatch(numberOfThreads);
        for (int i = 0; i < numberOfThreads; i++) {
            var terminalWorkerWrapper
                    = TerminalWorkerRepeatingWrapper.createTerminalWorkerWrapper(i, terminalAssignmentService, 10, 100);
            service.execute(() -> {
                terminalWorkerWrapper.run();
                latch.countDown();
            });
        }
        for (int i = 0; i < numberOfFlights; i++) {
            var flight = new Flight();
            flight.setFlightNumber("BBB" + i);
            flight.setAircraftType("Airbus A320 (twin-jet)");
            flight.setOrigin("Antalija AYT");
            flight.setDestination("Vilnius VNO");
            service.execute(() -> {
                flightService.registerFlight(flight);
                flightUpdateService.registerArrival(flight.getFlightNumber());
            });
        }
        latch.await();
        String actualNextFlight = arrivedFlightsService.getNextArrivedFlight();
        assertNull(actualNextFlight, "Arrived flight left not taken by terminal");
    }

    @Test
    public void testCanAssignFlightsWhenFlightsNotArriving() throws InterruptedException {
        var numberOfFlights = 5;
        for (int i = 0; i < numberOfFlights; i++) {
            var flight = new Flight();
            flight.setFlightNumber("AAA" + i);
            flight.setAircraftType("Airbus A320 (twin-jet)");
            flight.setOrigin("Antalija AYT");
            flight.setDestination("Vilnius VNO");
            flightService.registerFlight(flight);
            flightUpdateService.registerArrival(flight.getFlightNumber());
        }

        var numberOfThreads = 5;
        var service = Executors.newFixedThreadPool(numberOfThreads);
        var latch = new CountDownLatch(numberOfThreads);
        for (int i = 0; i < numberOfThreads; i++) {
            var terminalWorkerWrapper
                    = TerminalWorkerRepeatingWrapper.createTerminalWorkerWrapper(i, terminalAssignmentService, 1, 10);
            service.execute(() -> {
                terminalWorkerWrapper.run();
                latch.countDown();
            });
        }
        latch.await();
        String actualNextFlight = arrivedFlightsService.getNextArrivedFlight();
        assertNull(actualNextFlight, "Arrived flight left not taken by terminal");
    }


    @Test
    public void testRunTenTerminalsWithoutArrivedFlights() throws InterruptedException {
        var numberOfThreads = 5;
        var service = Executors.newFixedThreadPool(numberOfThreads);
        var latch = new CountDownLatch(numberOfThreads);
        for (int i = 0; i < numberOfThreads; i++) {
            var terminalWorkerWrapper
                    = TerminalWorkerRepeatingWrapper.createTerminalWorkerWrapper(i, terminalAssignmentService, 10, 10);
            service.execute(() -> {
                terminalWorkerWrapper.run();
                latch.countDown();
            });
        }
        latch.await();
    }

    private void sleepHandleInterruptedException(long millis) {
        try {
            Thread.currentThread().sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    @Configuration
    @ComponentScan(basePackages = {"edu.flightregistration.core"})
    public static class TestConfig {
    }
}
