package edu.flightregistration.core.services.sync;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Import(ArrivedFlightsServiceTest.TestConfig.class)
class ArrivedFlightsServiceTest {

    @Autowired
    ArrivedFlightsService arrivedFlightsService;

    @Test
    void canAutowire() {
        assertNotNull(arrivedFlightsService, "Can not autowire service");
    }

    @Test
    void canAddAndRemoveArrivedFlights() {
        var actualFlightNumberForEmptyService = arrivedFlightsService.getNextArrivedFlight();
        assertNull(actualFlightNumberForEmptyService, "Flight number for empty service is not null");

        var firstFlightNumber = "A0001";
        arrivedFlightsService.addArrivedFlight(firstFlightNumber);

        var actualFirstFlightNumber = arrivedFlightsService.getNextArrivedFlight();
        assertEquals(firstFlightNumber, actualFirstFlightNumber, "Added and retrieved flight numbers not equal");

        actualFlightNumberForEmptyService = arrivedFlightsService.getNextArrivedFlight();
        assertNull(actualFlightNumberForEmptyService, "Flight number for empty service is not null");

        var secondFlightNumber = "A0002";
        arrivedFlightsService.addArrivedFlight(secondFlightNumber);
        arrivedFlightsService.addArrivedFlight(firstFlightNumber);

        var actualSecondFlightNumber = arrivedFlightsService.getNextArrivedFlight();
        assertEquals(secondFlightNumber, actualSecondFlightNumber, "Added first flight number is not retrieved first");

        actualFirstFlightNumber = arrivedFlightsService.getNextArrivedFlight();
        assertEquals(firstFlightNumber, actualFirstFlightNumber, "Added second flight number is not retrieved second");
    }

    @Configuration
    @ComponentScan(basePackages = {"edu.flightregistration.core"})
    public static class TestConfig {
    }

}