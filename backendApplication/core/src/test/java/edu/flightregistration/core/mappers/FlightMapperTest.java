package edu.flightregistration.core.mappers;

import edu.flightregistration.core.domain.Flight;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@Import(FlightMapperTest.TestConfig.class)
class FlightMapperTest {

    @Autowired
    FlightMapper flightMapper;

    @Test
    public void canAutowireMapper() {
        assertNotNull(flightMapper, "Can not autowire flightMapper");
    }

    @Test
    public void canMapEntityToDomain() {
        var flightId = "W68002";
        var entityFlight = new edu.flightregistration.core.jpa.entity.Flight();
        entityFlight.setId(flightId);

        var actual = flightMapper.entityToDomain(entityFlight);

        assertNotNull(actual, "Domain flight is null after mapping");
        assertEquals(flightId, actual.getFlightNumber(), "Entity id is not equal to domain flight number");
    }

    @Test
    public void canMapDomainToEntity() {
        var flightNumber = "W68002";
        var domainFlight = new Flight();
        domainFlight.setFlightNumber(flightNumber);

        var actual = flightMapper.domainToEntity(domainFlight);

        assertNotNull(actual, "Entity flight is null after mapping");
        assertEquals(flightNumber, actual.getId(), "Flight number is not equal to entity id");
    }

    @Configuration
    @ComponentScan(basePackages = {"edu.flightregistration.core.mappers"})
    public static class TestConfig {
    }
}