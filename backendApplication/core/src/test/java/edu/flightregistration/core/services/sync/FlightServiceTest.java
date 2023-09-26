package edu.flightregistration.core.services.sync;

import edu.flightregistration.core.domain.Flight;
import edu.flightregistration.core.domain.FlightStatus;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Import(FlightServiceTest.TestConfig.class)
class FlightServiceTest {

    @Autowired
    FlightService flightService;

    @Autowired
    FlightUpdateService flightUpdateService;

    @Test
    void canAutowire() {

        assertNotNull(flightService, "Service can not be autowired");
    }

    @Test
    void canRegisterFlight() {
        var flight = new Flight();
        flight.setFlightNumber("X98000");
        flight.setAircraftType("Airbus A320 (twin-jet)");
        flight.setOrigin("Antalija AYT");
        flight.setDestination("Vilnius VNO");

        assertDoesNotThrow(
                () -> flightService.registerFlight(flight));
    }

    @Test
    void canGetFlightStatus() {

        var flightNumber = "X98001";
        var flight = new Flight();
        flight.setFlightNumber(flightNumber);
        flight.setAircraftType("Airbus A320 (twin-jet)");
        flight.setOrigin("Antalija AYT");
        flight.setDestination("Vilnius VNO");

        flightService.registerFlight(flight);

        FlightStatus actualScheduled = flightService.getFlightStatus(flightNumber);
        assertEquals(FlightStatus.SCHEDULED, actualScheduled, "Flight status is not scheduled");

        flightUpdateService.registerArrival(flightNumber);

        FlightStatus actualArrived = flightService.getFlightStatus(flightNumber);
        assertEquals(FlightStatus.ARRIVED, actualArrived, "Flight status is not arrived");

        flightUpdateService.registerDeparture(flightNumber);

        FlightStatus actualDeparted = flightService.getFlightStatus(flightNumber);
        assertEquals(FlightStatus.DEPARTED, actualDeparted, "Flight status is not departed");
    }

    @Test
    void canGetAllFlights() {

        var flight1 = new Flight();
        flight1.setFlightNumber("X98002");
        flight1.setAircraftType("Airbus A320 (twin-jet)");
        flight1.setOrigin("Antalija AYT");
        flight1.setDestination("Vilnius VNO");

        flightService.registerFlight(flight1);

        var flight2 = new Flight();
        flight2.setFlightNumber("X98003");
        flight2.setAircraftType("Airbus A320 (twin-jet)");
        flight2.setOrigin("Antalija AYT");
        flight2.setDestination("Vilnius VNO");

        flightService.registerFlight(flight2);

        var flights = flightService.getAllRegisteredFlights();
        assertTrue(flights.size() > 1, "Size of all flights list is less than one");
    }

    @Test
    void canFindFlightsByOriginAndOrDestination() {
        var flight1 = new Flight();
        flight1.setFlightNumber("X98004");
        flight1.setAircraftType("Airbus A320 (twin-jet)");
        flight1.setOrigin("Paphos PFO");
        flight1.setDestination("Berlin BER");

        flightService.registerFlight(flight1);

        var flight2 = new Flight();
        flight2.setFlightNumber("X98005");
        flight2.setAircraftType("Airbus A320 (twin-jet)");
        flight2.setOrigin("London LTN");
        flight2.setDestination("Berlin BER");

        flightService.registerFlight(flight2);

        var flight3 = new Flight();
        flight3.setFlightNumber("X98006");
        flight3.setAircraftType("Airbus A320 (twin-jet)");
        flight3.setOrigin("Berlin BER");
        flight3.setDestination("Oslo OSL");

        flightService.registerFlight(flight3);

        List<Flight> actualBerlinFlights = flightService.findFlightsByOriginOrDestination("Berlin", "Berlin");

        assertEquals(3, actualBerlinFlights.size(), "Number of found Berlin flights is not 3");

        List<Flight> actualFromLondonOrToBerlinFlights = flightService.findFlightsByOriginOrDestination("London", "Berlin");

        assertEquals(2, actualFromLondonOrToBerlinFlights.size(), "Number of from Oslo or to Berlin flights is not 2");

        List<Flight> actualFromBerlinAndToOsloFlights = flightService.findFlightsByOriginAndDestination("Berlin", "Oslo");

        assertEquals(1, actualFromBerlinAndToOsloFlights.size(), "Number of from Berlin and to Oslo flights is not 1");
    }

    @Configuration
    @ComponentScan(basePackages = {"edu.flightregistration.core"})
    public static class TestConfig {
    }
}