package edu.flightregistration.core.jpa.repository;

import edu.flightregistration.core.jpa.entity.Flight;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaSystemException;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Import(FlightRepositoryTest.TestConfig.class)
class FlightRepositoryTest {

    @Autowired
    FlightRepository flightRepository;

    @Test
    void testCanAutowire() {
        assertNotNull(flightRepository, "Unable to autowire repository");
    }

    @Test
    void testCanSaveFlight() {
        var flight = new Flight();
        flight.setId("FR4444");
        flight.setAircraftType("Airbus A320 (twin-jet)");
        flight.setOrigin("Antalija AYT");
        flight.setDestination("Vilnius VNO");

        assertDoesNotThrow(
                () -> flightRepository.save(flight));
    }

    @Test
    void testThrowsExceptionWhenIdIsNull() {
        var flight = new Flight();
        flight.setId(null);

        assertThrows(JpaSystemException.class,
                () -> flightRepository.save(flight));
    }

    @Test
    void testCanSaveAndGetFlight() {
        var flightId = "LH886";

        var flight = new Flight();
        flight.setId(flightId);
        flight.setAircraftType("Airbus A320 (twin-jet)");
        flight.setOrigin("Antalija AYT");
        flight.setDestination("Vilnius VNO");

        flightRepository.save(flight);

        var actual = flightRepository.findById(flightId);

        assertTrue(actual.isPresent(), String.format("Flight not found by id %s", flightId));
        assertEquals(flightId, actual.get().getId(), "Id of loaded flight is invalid");
    }

    @Configuration
    @EntityScan(basePackages = "edu.flightregistration.core.jpa.entity")
    @EnableJpaRepositories("edu.flightregistration.core.jpa.repository")
    @AutoConfigureDataJpa
    public static class TestConfig {
    }
}